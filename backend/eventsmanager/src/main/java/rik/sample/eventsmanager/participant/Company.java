package rik.sample.eventsmanager.participant;


import jakarta.validation.constraints.NotEmpty;

public record Company (
     Integer id,
    @NotEmpty
     String companyName,
    @NotEmpty
     String companyCode,
     Integer participantCount,
    @NotEmpty
     String paymentMethod,
     String info
) {}
