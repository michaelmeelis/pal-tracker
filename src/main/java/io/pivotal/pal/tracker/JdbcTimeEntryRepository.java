package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate template;

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? new TimeEntryMapper().mapRow(rs, 1) : null;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (`project_id`, `user_id`, `date`, `hours`) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setString(3, timeEntry.getDate());
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(Long id) {
        return template.query(
                "SELECT id, project_id, user_id, `date`, hours FROM time_entries WHERE id = ?",
                new Object[]{id},
                extractor

        );
    }

    @Override
    public List<TimeEntry> list() {
        return template.query(
                "SELECT id, project_id, user_id, `date`, hours FROM time_entries",
                new TimeEntryMapper()
        );
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        template.update(
                "UPDATE time_entries SET project_id = ?, user_id = ?, `date` = ? , hours = ? WHERE id = ?",
                timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours(), id
        );

        return find(id);
    }

    @Override
    public void delete(Long id) {
        template.update("DELETE FROM time_entries WHERE id = ?", id);
    }
}
