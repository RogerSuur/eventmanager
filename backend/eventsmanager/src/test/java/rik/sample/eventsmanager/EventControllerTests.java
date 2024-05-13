package rik.sample.eventsmanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import rik.sample.eventsmanager.events.Event;
import rik.sample.eventsmanager.events.EventController;
import rik.sample.eventsmanager.events.EventRepository;

import org.springframework.beans.factory.annotation.Autowired;


@WebMvcTest(EventController.class)
public class EventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAllEvents_whenGetEvents_thenReturnJsonArray() throws Exception {
        Event event1 = new Event(1, "Event 1", LocalDateTime.of(2024, 5, 31, 20,0), "City Hall", "Annual celebration");
        Event event2 = new Event(2, "Event 2", LocalDateTime.of(2024,12,31, 21,0), "Town Square", "Community gathering");

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].title").value("Event 2"));
    }

    @Test
    public void getEventById_whenGetSingleEvent_thenReturnEvent() throws Exception {
        Event event = new Event(1, "Event 1", LocalDateTime.of(2024, 5, 31, 20, 0), "City Hall", "Annual celebration");
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        mockMvc.perform(get("/api/events/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Event 1"));
    }

    @Test
    public void deleteEvent_whenDeleteEvent_thenStatusNoContent() throws Exception {
        int eventId = 1;
        doNothing().when(eventRepository).delete(eventId);

        mockMvc.perform(delete("/api/events/{id}", eventId))
                .andExpect(status().isNoContent());
    }

    
}