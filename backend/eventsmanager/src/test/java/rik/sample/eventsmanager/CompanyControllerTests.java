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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.containsString;
import rik.sample.eventsmanager.participant.Company;
import rik.sample.eventsmanager.participant.CompanyController;
import rik.sample.eventsmanager.participant.CompanyRepository;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private CompanyRepository companyRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CompanyController(companyRepository)).build();
    }

    @Test
    public void getAllCompanies_whenGetMethod_thenReturnJsonArray() throws Exception {
        Company company1 = new Company(1, "Company A", "123", 10, "card", "details");
        Company company2 = new Company(2, "Company B", "456", 20, "cash", "more details");

        when(companyRepository.findAll()).thenReturn(Arrays.asList(company1, company2));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Company A"))
                .andExpect(jsonPath("$[1].companyName").value("Company B"));
    }

    @Test
    public void getCompanyById_whenGetMethod_thenReturnCompany() throws Exception {
        Company company = new Company(1, "Company A", "123", 10, "card", "details");
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        mockMvc.perform(get("/api/companies/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Company A"));
    }

    @Test
    public void createCompany_whenPostMethod_thenReturnCreated() throws Exception {
        Company company = new Company(null, "New Company", "789", 5, "cash", "info");
        when(companyRepository.create(any(Company.class))).thenReturn(Optional.of(1));

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/companies/1")));
    }

    @Test
    public void updateCompany_whenPutMethod_thenReturnOk() throws Exception {
        Company updatedCompany = new Company(1, "Updated Company", "123", 10, "card", "updated details");
        when(companyRepository.update(updatedCompany, 1)).thenReturn(true);

        mockMvc.perform(put("/api/companies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedCompany)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCompany_whenDeleteMethod_thenReturnNoContent() throws Exception {
        doNothing().when(companyRepository).delete(1);

        mockMvc.perform(delete("/api/companies/{id}", 1))
                .andExpect(status().isNoContent());
    }
}

