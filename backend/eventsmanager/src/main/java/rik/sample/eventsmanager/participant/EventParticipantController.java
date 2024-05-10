package rik.sample.eventsmanager.participant;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/eventParticipant")
public class EventParticipantController {
    private final EventParticipantRepository eventParticipantRepository;

    public EventParticipantController(EventParticipantRepository eventParticipantRepository) {
        this.eventParticipantRepository = eventParticipantRepository;
    }

     // Get all eventParticipants
    @GetMapping("")
    public List<EventParticipant> getAllEventParticipants() {
        return eventParticipantRepository.findAll();
    }

    //TODO:Is really needed?
    // Get a single customer by ID
    // @GetMapping("/{id}")
    // public ResponseEntity<EventParticipant> getEventParticipantById(@PathVariable Integer id) {
    //     Optional<EventParticipant> eventParticipant = eventParticipantRepository.findById(id);
    //     return eventParticipant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // Create a new customer
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void createEventParticipant(@Valid @RequestBody EventParticipant eventParticipant) {
        eventParticipantRepository.create(eventParticipant);
    }


    //TODO:also needs to be check if needed
    // Update an existing customer
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // @PutMapping("/{id}")
    // void updateEventParticipant(@Valid @RequestBody EventParticipant eventParticipant, @PathVariable Integer id) {
    //     EventParticipantRepository.update(eventParticipant, id);
    // }

    //delete
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        eventParticipantRepository.delete(id);
    }

}
