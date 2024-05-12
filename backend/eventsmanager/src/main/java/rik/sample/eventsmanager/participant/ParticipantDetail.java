package rik.sample.eventsmanager.participant;

public class ParticipantDetail {
    private final Integer id;
    private final String name;
    private final String code;
    private final String type;

    public ParticipantDetail(Integer id, String name, String code, String type) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}