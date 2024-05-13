package rik.sample.eventsmanager.participant;

import jakarta.validation.constraints.NotEmpty;

public record Customer(

Integer id,
@NotEmpty
 String firstName,
@NotEmpty
 String lastName,
@NotEmpty
 String idCode,
@NotEmpty
 String paymentMethod,
 String info) {
   
    
}
