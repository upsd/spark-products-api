package domain;

import java.util.Objects;
import java.util.UUID;

public class Product {

    private UUID uuid;
    private String name;
    private double price;

    public Product(UUID uuid, String name, double price) {
        this.uuid = uuid;
        this.name = name;
        this.price = price;
    }

    public String name() {
        return name;
    }

    public double price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                Objects.equals(uuid, product.uuid) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, price);
    }

    public UUID id() {
        return this.uuid;
    }
}
