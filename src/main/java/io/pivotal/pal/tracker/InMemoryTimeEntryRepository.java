package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private HashMap<Long, TimeEntry> timeEntries;

    public InMemoryTimeEntryRepository() {
        this.timeEntries = new HashMap<>();
    }

    public InMemoryTimeEntryRepository(TimeEntry timeEntry) {
        this.timeEntries = new HashMap<>();
        timeEntry.setId(0);
        this.timeEntries.put(timeEntry.getId(), timeEntry);
    }

    public InMemoryTimeEntryRepository(HashMap<Long, TimeEntry> timeEntries){
        this.timeEntries = timeEntries;
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(timeEntries.size() + 1);
        timeEntries.put(timeEntry.getId(), timeEntry);

        return timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        return timeEntries.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        if (timeEntries.replace(id, timeEntry) != null) {
            timeEntry.setId(id);

            return timeEntry;
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        timeEntries.remove(id);

    }
}
