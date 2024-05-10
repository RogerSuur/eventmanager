package rik.sample.eventsmanager.events;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record Event(Integer id,
@NotNull
String title,
@NotNull
  LocalDateTime startTime, 
  @NotEmpty
  String location, 
  String info) {
 
    public Event {
        // if(startTime.isAfter(LocalDateTime.now())) {
        //     throw new IllegalArgumentException("Event cannot be in the past");
        // }
    }
}
