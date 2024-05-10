package rik.sample.eventsmanager.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

@Repository
public class CustomerRepository {
    private static final Logger log = LoggerFactory.getLogger(CustomerRepository.class);
    private final JdbcClient jdbcClient;

    public CustomerRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Customer> findAll() {
        return jdbcClient.sql("SELECT * FROM customers")
                         .query(Customer.class)
                         .list();
    }

    public Optional<Customer> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM customers WHERE id = :id")
                         .param("id", id)
                         .query(Customer.class)
                         .optional();
    }

    public void create(Customer customer) {
        System.out.println(customer);
        var updated = jdbcClient.sql("INSERT INTO customers ( first_name, last_name, id_code, payment_method, info) VALUES ( ?, ?, ?, ?, ?)")
                                .params(customer.firstName(), customer.lastName(), customer.idCode(), customer.paymentMethod(), customer.info())
                                .update();
        Assert.state(updated == 1, "Failed to create customer " + customer.firstName() + " " + customer.lastName());
    }

    public void saveAll(List<Customer> customers) {
        customers.stream().forEach(this::create);
    }

    public void update(Customer customer, Integer id) {
        var updated = jdbcClient.sql("update customers set first_name = ?, last_name = ?, id_code = ?, payment_method = ?,info=? where id = ?")
                .params(List.of(customer.firstName(), customer.lastName(), customer.idCode(),customer.paymentMethod(), customer.info(), id))
                .update();
        Assert.state(updated == 1, "Failed to update customer " + customer.firstName());
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("delete from customers where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete customer " + id);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int count() {
        //return jdbcClient.sql("SELECT COUNT(*) FROM customers").query().listOfRows().size();
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM customers", Integer.class);
        return count != null ? count : 0;
    }
}
