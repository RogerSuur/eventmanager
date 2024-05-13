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

    @GetMapping("")
    public List<EventParticipant> getAllEventParticipants() {
        return eventParticipantRepository.findAll();
    }

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

    // Create a new eventParticipant
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void createEventParticipant(@Valid @RequestBody EventParticipant eventParticipant) {
        eventParticipantRepository.create(eventParticipant);
    }


    //delete
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        eventParticipantRepository.delete(id);
    }

}
