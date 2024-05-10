package rik.sample.eventsmanager.participant;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class CompanyRepository {
    private static final Logger log = LoggerFactory.getLogger(CompanyRepository.class);
    private final JdbcClient jdbcClient;

    public CompanyRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Company> findAll() {
        return jdbcClient.sql("SELECT * FROM companies")
                         .query(Company.class)
                         .list();
    }

    public Optional<Company> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM companies WHERE id = :id")
                         .param("id", id)
                         .query(Company.class)
                         .optional();
    }

    public void create(Company company) {
        System.out.println(company);
        var updated = jdbcClient.sql("INSERT INTO companies ( company_name, company_code, participant_count, payment_method, info) VALUES ( ?, ?, ?, ?, ?)")
                                .params(company.companyName(), company.companyCode(), company.participantCount(), company.paymentMethod(), company.info())
                                .update();
        Assert.state(updated == 1, "Failed to create company " + company.companyName());
    }

    public void saveAll(List<Company> companies) {
        companies.stream().forEach(this::create);
    }

    public void update(Company company, Integer id) {
        var updated = jdbcClient.sql("update companies set company_name = ?, company_code = ?,participant_count=?, payment_method = ?,info=? where id = ?")
                .params(List.of(company.companyName(), company.companyCode(), company.participantCount(), company.paymentMethod(), company.info(), id))
                .update();
        Assert.state(updated == 1, "Failed to update company " + company.companyName());
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("delete from companies where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete ccompany " + id);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int count() {
        //return jdbcClient.sql("SELECT COUNT(*) FROM customers").query().listOfRows().size();
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM companies", Integer.class);
        return count != null ? count : 0;
    }
}
