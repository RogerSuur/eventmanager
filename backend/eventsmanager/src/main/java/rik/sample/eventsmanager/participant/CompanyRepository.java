package rik.sample.eventsmanager.participant;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    public Optional<Integer> create(Company company) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO companies (company_name, company_code, participant_count, payment_method, info) VALUES (?, ?, ?, ?, ?)",
                new String[]{"id"}  
            );
        ps.setString(1, company.companyName());
        ps.setString(2, company.companyCode());
        ps.setInt(3, company.participantCount());
        ps.setString(4, company.paymentMethod());
        ps.setString(5, company.info());
        return ps;
    }, keyHolder);

    log.info("Added company to the repository");

    if (keyHolder.getKeys().size() > 1) {
        return Optional.empty();
    } else if (keyHolder.getKey() == null) {
        return Optional.empty();
    } else {
        return Optional.of(keyHolder.getKey().intValue());
    }
    }

    public void saveAll(List<Company> companies) {
        companies.stream().forEach(this::create);
    }

    public boolean update(Company company, Integer id) {
        var updated = jdbcClient.sql("update companies set company_name = ?, company_code = ?,participant_count=?, payment_method = ?,info=? where id = ?")
                .params(List.of(company.companyName(), company.companyCode(), company.participantCount(), company.paymentMethod(), company.info(), id))
                .update();
                return updated ==1;
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
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM companies", Integer.class);
        return count != null ? count : 0;
    }
}
