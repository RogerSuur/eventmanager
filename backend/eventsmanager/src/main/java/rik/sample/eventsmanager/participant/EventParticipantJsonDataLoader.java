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
public class EventParticipantJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EventParticipantJsonDataLoader.class);
    private final EventParticipantRepository eventParticipantRepository;
    private final ObjectMapper objectMapper;

    public EventParticipantJsonDataLoader(EventParticipantRepository eventParticipantRepository, ObjectMapper objectMapper) {
        this.eventParticipantRepository = eventParticipantRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (eventParticipantRepository.count() == 0) {
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/eventParticipants.json")) {
                EventParticipants allEventParticipants = objectMapper.readValue(inputStream, EventParticipants.class);
                log.info("Reading {} eventParticipants from JSON data and saving to the database.", allEventParticipants.eventParticipants().size());
                eventParticipantRepository.saveAll(allEventParticipants.eventParticipants());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            log.info("Not loading eventparticipants from JSON data because the collection already contains data.");
        }
    }
}
