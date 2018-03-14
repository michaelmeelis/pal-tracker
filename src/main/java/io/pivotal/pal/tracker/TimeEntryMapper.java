package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeEntryMapper implements RowMapper<TimeEntry> {
    @Override
    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimeEntry timeEntry = new TimeEntry();

        timeEntry.setId(rs.getInt("id"));
        timeEntry.setDate(rs.getString("date"));
        timeEntry.setHours(rs.getInt("hours"));
        timeEntry.setProjectId(rs.getLong("project_id"));
        timeEntry.setUserId(rs.getLong("user_id"));

        return timeEntry;
    }
}
