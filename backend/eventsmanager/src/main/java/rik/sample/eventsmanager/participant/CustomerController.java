package rik.sample.eventsmanager.participant;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
     private final CustomerRepository customerRepository;

    private static final Logger log = LoggerFactory.getLogger(CustomerRepository.class);

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Get all customers
    @GetMapping("")
    public List<Customer> getAllCustomers() {
        log.info("COntroller receive getall customers");
        return customerRepository.findAll();
    }

    // Get a single customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new customer
    // @ResponseStatus(HttpStatus.CREATED)
    // @PostMapping("")
    // void createCustomer(@Valid @RequestBody Customer customer) {
    //     customerRepository.create(customer);
    // }
     @PostMapping("")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody Customer customer) {
        log.info("COntroller receive post customer", customer);
    Optional<Integer> customerId = customerRepository.create(customer);
    if (customerId.isPresent()) {
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(customerId.get())
            .toUri();
        return ResponseEntity.created(location).body(Map.of("id", customerId.get()));
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create customer");
    }
}


    // Update an existing customer
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody Customer customer, @PathVariable Integer id) {
        log.info("COntroller receive update customer", customer);
        boolean isUpdated = customerRepository.update(customer, id);
        if (isUpdated) {
            log.info("Repository updated customer", customer);
            return ResponseEntity.ok().build();
        } else {
            log.info("Repository couldnt update customer", customer);
            return ResponseEntity.notFound().build();
        }
    }

    //delete
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        customerRepository.delete(id);
    }

}
