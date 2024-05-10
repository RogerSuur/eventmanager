package rik.sample.eventsmanager.participant;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class CompanyJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CompanyJsonDataLoader.class);
    private final CompanyRepository companyRepository;
    private final ObjectMapper objectMapper;

    public CompanyJsonDataLoader (CompanyRepository companyRepository, ObjectMapper objectMapper) {
        this.companyRepository = companyRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public void run(String... args) throws Exception {
        if(companyRepository.count()== 0) {
            try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/companies.json")) {
                Companies allCompanies = objectMapper.readValue(inputStream, Companies.class);
                log.info("Reading {} events from JSON data and saving to a database.", allCompanies.companies().size());
                companyRepository.saveAll(allCompanies.companies());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            log.info("Not loading companies form JSON data because the collection contains data.");
        }
    }
}
