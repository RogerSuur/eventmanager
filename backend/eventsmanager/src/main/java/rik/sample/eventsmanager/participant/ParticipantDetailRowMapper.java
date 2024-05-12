package rik.sample.eventsmanager.participant;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantDetailRowMapper implements RowMapper<ParticipantDetail> {
    @Override
    public ParticipantDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ParticipantDetail(
            rs.getInt("participant_id"),
            rs.getString("name"),
            rs.getString("code"),
            rs.getString("participant_type")
        );
    }
}
