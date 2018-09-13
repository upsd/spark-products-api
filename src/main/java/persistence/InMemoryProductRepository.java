package persistence;

import domain.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class InMemoryProductRepository {

    private final List<Product> products;

    public InMemoryProductRepository() {
        this.products = new ArrayList<>();
    }

    public List<Product> getAll() {
        return products;
    }

    public void add(Product product) {
        boolean idAlreadyUsed = products.stream().anyMatch(p -> p.id().equals(product.id()));

        if (idAlreadyUsed) {
            throw new ProductAlreadyExistsException();
        }

        products.add(product);
    }

    public void clear() {
        products.clear();
    }

    public Optional<Product> getById(UUID id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst();
    }
}
