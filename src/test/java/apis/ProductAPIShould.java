package apis;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import domain.Product;
import org.junit.Before;
import org.junit.Test;
import persistence.InMemoryProductRepository;
import spark.Request;
import spark.Response;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ProductAPIShould {

    private InMemoryProductRepository inMemoryProductRepository;
    private ProductAPI productAPI;
    private Request req;
    private Response res;

    @Before
    public void setUp() {
        inMemoryProductRepository = mock(InMemoryProductRepository.class);
        req = mock(Request.class);
        res = mock(Response.class);
        productAPI = new ProductAPI(inMemoryProductRepository);
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
                new Product("a product", 10.00),
                new Product("another product", 10.00)
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
        Product newProduct = new Product("new product", 15.00);

        given(req.body()).willReturn(jsonObjectFor(newProduct).toString());


        String response = productAPI.create(req, res);


        verify(inMemoryProductRepository).add(newProduct);
        verify(res).status(201);
        assertThat(response, is(""));
    }

    @Test
    public void not_create_duplicate_products() {
        Product alreadyExistingProduct = new Product("new product", 15.00);

        given(inMemoryProductRepository.getAll()).willReturn(asList(alreadyExistingProduct));
        given(req.body()).willReturn(jsonObjectFor(alreadyExistingProduct).toString());


        String response = productAPI.create(req, res);


        verify(inMemoryProductRepository, never()).add(alreadyExistingProduct);
        verify(res).status(400);
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
                .add("name", p.name())
                .add("price", p.price());
    }
}
