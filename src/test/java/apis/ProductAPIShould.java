package apis;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import domain.Product;
import helpers.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import persistence.InMemoryProductRepository;
import spark.Request;
import spark.Response;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProductAPIShould {

    private InMemoryProductRepository inMemoryProductRepository;
    private ProductAPI productAPI;
    private Request req;
    private Response res;
    private IdGenerator idGenerator;

    @Before
    public void setUp() {
        inMemoryProductRepository = mock(InMemoryProductRepository.class);
        req = mock(Request.class);
        res = mock(Response.class);
        idGenerator = mock(IdGenerator.class);
        productAPI = new ProductAPI(inMemoryProductRepository, idGenerator);
    }

    @Test
    public void return_all_products_when_no_products_found() {
        List<Product> emptyProducts = Collections.emptyList();
        given(inMemoryProductRepository.getAll()).willReturn(emptyProducts);


        String expectedResponse = jsonStringFor(emptyProducts);

        String response = productAPI.getAll(req, res);


        verify(res).type("application/json");
        verify(res).status(200);
        assertThat(response, is(expectedResponse));
    }

    @Test
    public void return_all_products_when_multiple_products_are_found() {
        List<Product> products = asList(
                new Product(UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2"),"a product", 10.00),
                new Product(UUID.fromString("ea0545c5-e0a1-46cd-80fe-26fef5c9ccb8"),"another product", 10.00)
        );
        given(inMemoryProductRepository.getAll()).willReturn(products);


        String expectedResponse = jsonStringFor(products);

        String response = productAPI.getAll(req, res);


        verify(res).type("application/json");
        verify(res).status(200);
        assertThat(response, is(expectedResponse));
    }

    @Test
    public void create_a_product() {
        Product newProduct = new Product(UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2"), "new product", 15.00);

        given(req.body()).willReturn(jsonObjectFor(newProduct).toString());
        given(idGenerator.generate()).willReturn(UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2"));

        String response = productAPI.create(req, res);


        verify(inMemoryProductRepository).add(newProduct);
        verify(res).status(201);
        assertThat(response, is(""));
    }

    private String jsonStringFor(List<Product> products) {
        JsonArray productsArray = new JsonArray();

        products.forEach(p -> {
            JsonObject product = jsonObjectFor(p);

            productsArray.add(product);
        });

        return new JsonObject()
                .add("products", productsArray).toString();
    }

    private JsonObject jsonObjectFor(Product p) {
        return new JsonObject()
                .add("id", p.id().toString())
                .add("name", p.name())
                .add("price", p.price());
    }
}
