import apis.ProductAPI;
import apis.Routes;
import domain.Product;
import org.junit.*;
import persistence.InMemoryProductRepository;
import spark.Spark;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class AcceptanceTests {

    private static InMemoryProductRepository inMemoryProductRepository;

    @BeforeClass
    public static void setUp() {
        inMemoryProductRepository = new InMemoryProductRepository();
        ProductAPI productAPI = new ProductAPI(inMemoryProductRepository);

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
        inMemoryProductRepository.add(new Product("blah", 15.00));
        inMemoryProductRepository.add(new Product("blah 2", 20.00));


        get("/products").then()
                .statusCode(200)
                .contentType("application/json")
                .body("products[0].name", is("blah"))
                .body("products[0].price", is(15))
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
    public void return_400_when_attempting_to_create_already_existing_product() {
        inMemoryProductRepository.add(new Product("new product", 20));


        given()
                .contentType("application/json").body("{ \"name\": \"new product\", \"price\": 20.00 }")
                .when()
                .post("/products")
                .then()
                .statusCode(400);
    }
}
