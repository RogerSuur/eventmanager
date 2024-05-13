package rik.sample.eventsmanager.participant;

public record EventParticipant (
     Integer eventId,
     Integer participantId,
     String participantType
) {
   
}
