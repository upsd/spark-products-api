package apis;

import static spark.Spark.get;
import static spark.Spark.post;

public class Routes {

    public void wireUsing(ProductAPI productAPI) {
        get("products", (req, res) -> productAPI.getAll(req, res));
        get("products/:id", (req, res) -> productAPI.getById(req, res));
        post("products", (req, res) -> productAPI.create(req, res));
    }
}
