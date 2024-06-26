package rik.sample.eventsmanager.events;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record Event(
Integer id,
@NotEmpty
String title,
@NotNull
  LocalDateTime startTime, 
  @NotNull
  String location, 
  String info) {
 
}
