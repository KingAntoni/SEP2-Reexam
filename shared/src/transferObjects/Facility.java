package transferObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Facility implements Serializable {
    private int id;
    private String title;
    private String description;
    private List<Schedule> schedules;

    public Facility(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.schedules = new ArrayList<>();
    }

    public Facility(String title, String description) {
        this.title = title;
        this.description = description;
        this.schedules = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "Facility [id=" + id + ", title=" + title + ", description=" + description + ", schedules=" + schedules + "]";
    }
}

