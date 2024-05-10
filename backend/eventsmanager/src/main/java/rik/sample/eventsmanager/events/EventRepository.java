package rik.sample.eventsmanager.events;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class EventRepository {


    private static final Logger log = LoggerFactory.getLogger(EventRepository.class);
    private final JdbcClient jdbcClient;

    public EventRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Event> findAll() {
        log.info("List events");
        return jdbcClient.sql("select * from events")
                .query(Event.class)
                .list();
    }

    public Optional<Event> findById(Integer id) {
        return jdbcClient.sql("SELECT id,title,startTime,location, info FROM events WHERE id = :id" )
                .param("id", id)
                .query(Event.class)
                .optional();
    }

    public void create(Event event) {
        var updated = jdbcClient.sql("INSERT INTO events(id,title,startTime, location, info) values(?,?,?,?,?)")
                .params(List.of(event.id(),event.title(),event.startTime(),event.location().toString(), event.info()))
                .update();

        Assert.state(updated == 1, "Failed to create event " + event.title());
    }

    public void update(Event event, Integer id) {
        var updated = jdbcClient.sql("update events set title = ?, startTime = ?,location = ?, info=? where id = ?")
                .params(List.of(event.title(),event.startTime(),event.location().toString(), event.info(), id))
                .update();

        Assert.state(updated == 1, "Failed to update event " + event.title());
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("delete from events where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete event " + id);
    }

    public int count() {
        return jdbcClient.sql("select * from events").query().listOfRows().size();
    }

    public void saveAll(List<Event> events) {
        events.stream().forEach(this::create);
    }

    public List<Event> findByLocation(String location) {
        return jdbcClient.sql("select * from events where location = :location")
                .param("location", location)
                .query(Event.class)
                .list();
    }
}


// List <Event> findAll() {
//     return events;
// }

// Optional<Event> findById(Integer id) {
//     return events.stream()
//             .filter(event -> event.id() == id)
//             .findFirst();
//     }

// void create(Event event) {
//     events.add(event);
// }

// void update(Event event, Integer id) {
//     Optional<Event> existingEvent = findById(id);
//     if(existingEvent.isPresent()) {
//         events.set(events.indexOf(existingEvent.get()), event);
//     }
// }

// //delete
// void delete(Integer id) {
//     events.removeIf(event -> event.id().equals(id));
// }