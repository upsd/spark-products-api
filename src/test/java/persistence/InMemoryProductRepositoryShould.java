package persistence;

import domain.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        Product productToAdd = new Product("name", 10);
        repository.add(productToAdd);


        assertThat(repository.getAll(), is(asList(productToAdd)));
    }

    @Test
    public void clear_all_products() {
        repository.add(new Product("1", 20));
        repository.add(new Product("2", 10));


        repository.clear();


        assertThat(repository.getAll().size(), is(0));
    }
}
