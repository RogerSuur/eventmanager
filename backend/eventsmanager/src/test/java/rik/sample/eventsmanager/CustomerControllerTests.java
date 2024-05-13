package rik.sample.eventsmanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.Matchers.containsString;
import com.fasterxml.jackson.databind.ObjectMapper;

import rik.sample.eventsmanager.participant.Customer;
import rik.sample.eventsmanager.participant.CustomerController;
import rik.sample.eventsmanager.participant.CustomerRepository;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCustomers_whenGetCustomers_thenReturnJsonArray() throws Exception {
        Customer customer1 = new Customer(1, "John", "Doe", "1234", "card", "info");
        Customer customer2 = new Customer(2, "Jane", "Doe", "5678", "cash", "more info");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    public void getCustomerById_whenGetSingleCustomer_thenReturnCustomerJson() throws Exception {
        Customer customer = new Customer(1, "John", "Doe", "1234", "card", "info");
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/customers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void createCustomer_whenPostCustomer_thenStatusCreated() throws Exception {
        Customer customer = new Customer(null, "John", "Doe", "1234", "card", "info");
        when(customerRepository.create(any(Customer.class))).thenReturn(Optional.of(1));

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/customers/1")));
    }

    @Test
    public void deleteCustomer_whenDeleteCustomer_thenStatusNoContent() throws Exception {
        doNothing().when(customerRepository).delete(1);

        mockMvc.perform(delete("/api/customers/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
