package apis;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import domain.Product;
import persistence.InMemoryProductRepository;
import spark.Request;
import spark.Response;

public class ProductAPI {

    private InMemoryProductRepository inMemoryProductRepository;

    public ProductAPI(InMemoryProductRepository inMemoryProductRepository) {
        this.inMemoryProductRepository = inMemoryProductRepository;
    }

    public String getAll(Request req, Response res) {
        JsonArray products = new JsonArray();
        JsonObject responseBody = new JsonObject().add("products", products);

        populateJsonWithProducts(products);

        res.status(200);
        res.type("application/json");

        return responseBody.toString();
    }

    public String create(Request req, Response res) {
        JsonObject productToAdd = JsonObject.readFrom(req.body());
        Product product = productFrom(productToAdd);

        if (inMemoryProductRepository.getAll().contains(product)) {
            res.status(400);
            return "";
        }

        inMemoryProductRepository.add(product);
        res.status(201);
        return "";
    }

    private void populateJsonWithProducts(JsonArray products) {
        inMemoryProductRepository.getAll().forEach(p -> {
            JsonObject product = new JsonObject();
            product.add("name", p.name());
            product.add("price", p.price());

            products.add(product);
        });
    }
    
    private Product productFrom(JsonObject productAsJson) {
        String name = productAsJson.get("name").asString();
        double price = productAsJson.get("price").asDouble();

        return new Product(name, price);
    }
}
