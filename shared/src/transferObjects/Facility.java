package transferObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Facility implements Serializable {
    private String title;
    private String description;
    private List<Schedule> schedules;

    public Facility(String title, String description) {
        this.title = title;
        this.description = description;
        this.schedules = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }

    @Override
    public String toString() {
        return "Facility [title=" + title + ", description=" + description + ", schedules=" + schedules + "]";
    }
}
