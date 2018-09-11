import apis.ProductAPI;
import apis.Routes;
import persistence.InMemoryProductRepository;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        ProductAPI productAPI = new ProductAPI(new InMemoryProductRepository());
        Routes routes = new Routes();

        routes.wireUsing(productAPI);
    }
}
