import apis.ProductAPI;
import apis.Routes;
import com.eclipsesource.json.JsonObject;
import domain.Product;
import helpers.IdGenerator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import persistence.InMemoryProductRepository;
import spark.Spark;

import java.util.UUID;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.mock;

public class AcceptanceTests {

    private static InMemoryProductRepository inMemoryProductRepository;
    private static IdGenerator idGenerator;

    @BeforeClass
    public static void setUp() {
        inMemoryProductRepository = new InMemoryProductRepository();
        idGenerator = mock(IdGenerator.class);
        ProductAPI productAPI = new ProductAPI(inMemoryProductRepository, idGenerator);

        Spark.port(8080);
        new Routes().wireUsing(productAPI);
        Spark.awaitInitialization();
    }

    @Before
    public void resetRepository() {
        inMemoryProductRepository.clear();
    }

    @Test
    public void return_200_and_empty_collection_when_no_products_found() {
        get("/products").then()
                .body("products", empty())
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    public void return_200_and_collection_of_products_when_products_are_found() {
        inMemoryProductRepository.add(new Product(UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2"), "blah", 15.00));
        inMemoryProductRepository.add(new Product(UUID.fromString("ea0545c5-e0a1-46cd-80fe-26fef5c9ccb8"), "blah 2", 20.00));


        get("/products").then()
                .statusCode(200)
                .contentType("application/json")
                .body("products[0].id", is("81b573da-934e-4111-b63c-9bd0c0f644b2"))
                .body("products[0].name", is("blah"))
                .body("products[0].price", is(15))
                .body("products[1].id", is("ea0545c5-e0a1-46cd-80fe-26fef5c9ccb8"))
                .body("products[1].name", is("blah 2"))
                .body("products[1].price", is(20));
    }

    @Test
    public void return_201_when_product_is_created() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"new product\", \"price\": 20.00 }")
        .when()
                .post("/products")
        .then()
                .statusCode(201);
    }

    @Test
    public void return_404_when_product_with_specific_id_cannot_be_found() {
        get("/products/81b573da-934e-4111-b63c-9bd0c0f644b2").then()
                .statusCode(404);
    }

    @Test
    public void return_200_when_product_with_specific_id_can_be_found() {
        UUID id = UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2");
        inMemoryProductRepository.add(new Product(id, "chris", 30));


        get("/products/81b573da-934e-4111-b63c-9bd0c0f644b2").then()
                .body("id", is("81b573da-934e-4111-b63c-9bd0c0f644b2"))
                .body("name", is("chris"))
                .body("price", is(30))
                .statusCode(200);
    }

    @Test
    public void return_204_when_product_has_been_successfully_updated() {
        UUID id = UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2");
        inMemoryProductRepository.add(new Product(id, "cHrIs", 30));


        given()
                .contentType("application/json")
                .body("{ \"name\": \"new product\", \"price\": 20.00 }")
        .when()
                .put("/products/81b573da-934e-4111-b63c-9bd0c0f644b2")
        .then()
                .statusCode(204);
    }

    @Test
    public void return_404_when_attempting_to_update_non_existent_product() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"new product\", \"price\": 20.00 }")
        .when()
                .put("/products/81b573da-934e-4111-b63c-9bd0c0f644b2")
        .then()
                .statusCode(404);
    }

    @Test
    public void return_400_when_attempting_to_update_using_invalid_id() {
        String payload = new JsonObject()
                .add("name", "")
                .add("price", 20)
                .toString();

        given()
                .contentType("application/json")
                .body(payload)
        .when()
                .put("/products/wrong-number")
        .then()
                .statusCode(400);
    }
}
