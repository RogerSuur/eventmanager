package rik.sample.eventsmanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import rik.sample.eventsmanager.participant.EventParticipant;
import rik.sample.eventsmanager.participant.EventParticipantController;
import rik.sample.eventsmanager.participant.EventParticipantRepository;
import rik.sample.eventsmanager.participant.ParticipantDetail;

@WebMvcTest(EventParticipantController.class)
public class EventParticipantControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private EventParticipantRepository eventParticipantRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new EventParticipantController(eventParticipantRepository)).build();
    }

    @Test
    public void getAllEventParticipants_whenGetMethod_thenReturnJsonArray() throws Exception {
        EventParticipant participant1 = new EventParticipant(1, 1, "customer");
        EventParticipant participant2 = new EventParticipant(1, 2, "company");

        when(eventParticipantRepository.findAll()).thenReturn(Arrays.asList(participant1, participant2));

        mockMvc.perform(get("/api/eventParticipant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].participantType").value("customer"))
                .andExpect(jsonPath("$[1].participantType").value("company"));
    }

    @Test
    public void getEventParticipants_whenGetByEventId_thenReturnParticipantDetails() throws Exception {
        ParticipantDetail detail = new ParticipantDetail(1, "John Doe","1234", "customer");
        when(eventParticipantRepository.findParticipantsByEventId(1)).thenReturn(Collections.singletonList(detail));

        mockMvc.perform(get("/api/eventParticipant/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void createEventParticipant_whenPostMethod_thenReturnStatusCreated() throws Exception {
        EventParticipant newParticipant = new EventParticipant(1, 3, "customer");
        doNothing().when(eventParticipantRepository).create(any(EventParticipant.class));

        mockMvc.perform(post("/api/eventParticipant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newParticipant)))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteEventParticipant_whenDeleteMethod_thenReturnStatusNoContent() throws Exception {
        doNothing().when(eventParticipantRepository).delete(1);

        mockMvc.perform(delete("/api/eventParticipant/{id}", 1))
                .andExpect(status().isNoContent());
    }
}

