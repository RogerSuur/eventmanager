package rik.sample.eventsmanager.participant;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // TODO: IS REALLY NEEDED?
     // Get all eventParticipants
    @GetMapping("")
    public List<EventParticipant> getAllEventParticipants() {
        return eventParticipantRepository.findAll();
    }

    // Get a single customer by ID
    // public ResponseEntity<EventParticipant> findById(@PathVariable Integer id) {
        //     Optional<EventParticipant> eventParticipant = eventParticipantRepository.findById(id);
        //     System.out.println("Participant:" + eventParticipant);
        //     return eventParticipant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        // }
    @GetMapping("/{id}")
    public ResponseEntity<List<ParticipantDetail>> getEventParticipants(@PathVariable Integer id) {
        System.out.println("EVENTID " + id);
        List<ParticipantDetail> participants = eventParticipantRepository.findParticipantsByEventId(id);
        if (participants.isEmpty()) {
            System.out.println("ERROR, participants.empty");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participants);
    }

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
