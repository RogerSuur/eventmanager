package rik.sample.eventsmanager.participant;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class EventParticipantRepository {
        private final JdbcClient jdbcClient;

        public EventParticipantRepository(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

    public List<EventParticipant> findAll() {
        return jdbcClient.sql("SELECT * FROM event_participants")
            .query(EventParticipant.class)
            .list();
        }

    public void create(EventParticipant eventParticipant) {
        var updated = jdbcClient.sql("INSERT INTO event_participants ( event_id, participant_id, participant_type) VALUES (?, ?, ?)")
                                .params(eventParticipant.eventId(), eventParticipant.participantId(), eventParticipant.participantType())
                                .update();
        Assert.state(updated == 1, "Failed to create eventParticipant " + eventParticipant.participantId() + " " + eventParticipant.participantType() + " for event " + eventParticipant.eventId());
    }

    public void saveAll(List<EventParticipant> eventParticipants) {
        eventParticipants.stream().forEach(this::create);
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("delete from event_participants where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete event participant " + id);
    }

     @Autowired
    private JdbcTemplate jdbcTemplate;


    public int count() {
        //return jdbcClient.sql("SELECT COUNT(*) FROM customers").query().listOfRows().size();
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event_participants", Integer.class);
        return count != null ? count : 0;
    }
}