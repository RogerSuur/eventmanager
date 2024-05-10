package rik.sample.eventsmanager.participant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomerJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CustomerJsonDataLoader.class);
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public CustomerJsonDataLoader(CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) {
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/customers.json")) {
                Customers allCustomers = objectMapper.readValue(inputStream, Customers.class);
                log.info("Reading {} customers from JSON data and saving to the database.", allCustomers.customers().size());
                customerRepository.saveAll(allCustomers.customers());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            log.info("Not loading customers from JSON data because the collection already contains data.");
        }
    }
}
