package apis;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import domain.Product;
import helpers.IdGenerator;
import persistence.InMemoryProductRepository;
import persistence.ProductAlreadyExistsException;
import spark.Request;
import spark.Response;

import java.util.Optional;
import java.util.UUID;

public class ProductAPI {

    private InMemoryProductRepository inMemoryProductRepository;
    private IdGenerator idGenerator;

    public ProductAPI(InMemoryProductRepository inMemoryProductRepository, IdGenerator idGenerator) {
        this.inMemoryProductRepository = inMemoryProductRepository;
        this.idGenerator = idGenerator;
    }

    public String getAll(Request req, Response res) {
        JsonArray products = new JsonArray();
        JsonObject responseBody = new JsonObject().add("products", products);

        populateJsonWithProducts(products);

        res.status(200);
        res.type("application/json");

        return responseBody.toString();
    }

    public String create(Request req, Response res) throws ProductAlreadyExistsException {
        JsonObject productToAdd = JsonObject.readFrom(req.body());
        Product product = productFrom(productToAdd);

        inMemoryProductRepository.add(product);
        res.status(201);
        return "";
    }

    public String getById(Request req, Response res) {
        UUID id = UUID.fromString(req.params(":id"));

        Optional<Product> productFound = inMemoryProductRepository.getById(id);

        if (!productFound.isPresent()) {
            res.status(404);
            return "";
        }

        res.status(200);
        res.type("application/json");

        return getJsonFor(productFound.get()).toString();
    }

    public String update(Request req, Response res) {
        JsonObject payload = JsonObject.readFrom(req.body());

        UUID id = null;
        try {
            id = UUID.fromString(req.params(":id"));
        } catch (Exception e) {
            res.status(400);
            return "";
        }
        String name = payload.get("name").asString();
        double price = payload.get("price").asDouble();

        if (inMemoryProductRepository.getById(id).isPresent()) {
            inMemoryProductRepository.update(new Product(id, name, price));
            res.status(204);
            return "";
        }

        res.status(404);
        return "";
    }

    private void populateJsonWithProducts(JsonArray products) {
        inMemoryProductRepository.getAll().forEach(p -> {
            JsonObject product = getJsonFor(p);

            products.add(product);
        });
    }

    private JsonObject getJsonFor(Product p) {
        JsonObject product = new JsonObject();
        product.add("id", p.id().toString());
        product.add("name", p.name());
        product.add("price", p.price());
        return product;
    }

    private Product productFrom(JsonObject productAsJson) {
        String name = productAsJson.get("name").asString();
        double price = productAsJson.get("price").asDouble();

        return new Product(idGenerator.generate(), name, price);
    }
}
