package persistence;

import domain.Product;

import java.util.ArrayList;
import java.util.List;

public class InMemoryProductRepository {

    private final List<Product> products;

    public InMemoryProductRepository() {
        this.products = new ArrayList<>();
    }

    public List<Product> getAll() {
        return products;
    }

    public void add(Product product) {
        products.add(product);
    }

    public void clear() {
        products.clear();
    }
}
