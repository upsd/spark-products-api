package persistence;

import domain.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class InMemoryProductRepositoryShould {

    private InMemoryProductRepository repository;

    @Before
    public void setUp() {
        repository = new InMemoryProductRepository();
    }

    @Test
    public void be_empty_by_default() {
        List<Product> products = repository.getAll();


        assertThat(products.size(), is(0));
    }

    @Test
    public void add_a_product() {
        Product productToAdd = new Product(UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2"), "name", 10);
        repository.add(productToAdd);


        assertThat(repository.getAll(), is(asList(productToAdd)));
    }

    @Test
    public void clear_all_products() {
        repository.add(new Product(UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2"), "1", 20));
        repository.add(new Product(UUID.fromString("81b573da-934e-4111-b63c-9bd0c0f644b2"), "2", 10));


        repository.clear();


        assertThat(repository.getAll().size(), is(0));
    }
}
