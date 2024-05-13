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
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyRepository companyRepository;

        private static final Logger log = LoggerFactory.getLogger(CompanyRepository.class);

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Get all companies
    @GetMapping("")
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // Get a single company by ID
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCustomerById(@PathVariable Integer id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Object> createCompany(@Valid @RequestBody Company company) {
        log.info("COntroller receive post");
    Optional<Integer> companyId = companyRepository.create(company);
    if (companyId.isPresent()) {
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(companyId.get())
            .toUri();
        return ResponseEntity.created(location).body(Map.of("id", companyId.get()));
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create company");
    }
}


    // Update an existing company
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@Valid @RequestBody Company company, @PathVariable Integer id) {
        log.info("COntroller receive update company", company);
        companyRepository.update(company, id);
        boolean isUpdated = companyRepository.update(company, id);
        if (isUpdated) {
            log.info("Repository updated company", company);
            return ResponseEntity.ok().build();
        } else {
            log.info("Repository couldnt update company", company);
            return ResponseEntity.notFound().build();
        }
    }

    //delete
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        companyRepository.delete(id);
    }
}
