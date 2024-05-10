package rik.sample.eventsmanager.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;



@Component
public class EventJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EventJsonDataLoader.class);
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public EventJsonDataLoader(EventRepository eventRepository, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public void run(String... args) throws Exception {
        if(eventRepository.count()== 0) {
            try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/events.json")) {
                Events allEvents = objectMapper.readValue(inputStream, Events.class);
                log.info("Reading {} events from JSON data and saving to a database.", allEvents.events().size());
                eventRepository.saveAll(allEvents.events());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            log.info("Not loading Events form JSON data because the collection contains data.");
        }
    }
}