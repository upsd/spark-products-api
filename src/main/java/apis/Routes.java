package apis;

import static spark.Spark.*;

public class Routes {

    public void wireUsing(ProductAPI productAPI) {
        get("products", (req, res) -> productAPI.getAll(req, res));
        get("products/:id", (req, res) -> productAPI.getById(req, res));
        put("products/:id", (req, res) -> productAPI.update(req, res));
        post("products", (req, res) -> productAPI.create(req, res));
    }
}
