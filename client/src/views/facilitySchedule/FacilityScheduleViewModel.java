package views.facilitySchedule;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.SportFacilityModel;
import transferObjects.Schedule;
import transferObjects.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FacilityScheduleViewModel {
    private final SportFacilityModel model;
    private final StringProperty selectedDate;
    private final ObjectProperty<LocalTime> selectedHour;
    private final ObservableList<String> scheduleList;
    private int facilityId; // Add facilityId to track current facility

    public FacilityScheduleViewModel(SportFacilityModel model, int facilityId) {
        this.model = model;
        this.selectedDate = new SimpleStringProperty();
        this.selectedHour = new SimpleObjectProperty<>();
        this.scheduleList = FXCollections.observableArrayList();
        this.facilityId = facilityId;
    }

    public StringProperty selectedDateProperty() {
        return selectedDate;
    }

    public ObjectProperty<LocalTime> selectedHourProperty() {
        return selectedHour;
    }

    public ObservableList<String> getScheduleList() {
        return scheduleList;
    }

    public void loadSchedule(LocalDate date) {
        scheduleList.clear();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        // Mock data for now, assuming all slots are free except one hardcoded example
        List<Schedule> schedules = List.of(
                new Schedule(date.atTime(10, 0), date.atTime(11, 0), new User("reservedUser", "password"), facilityId)
        );
        for (int hour = 6; hour < 23; hour++) {
            LocalTime startTime = LocalTime.of(hour, 0);
            LocalTime endTime = startTime.plusHours(1);
            String status = schedules.stream().anyMatch(s -> s.getStartTime().equals(startTime)) ? "RESERVED" : "FREE";
            scheduleList.add(startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter) + " " + status);
        }
    }

    public void reserve(User user) {
        LocalDate date = LocalDate.parse(selectedDate.get());
        LocalTime startTime = selectedHour.get();
        LocalTime endTime = startTime.plusHours(1); // Assuming fixed 1-hour slots
        Schedule schedule = new Schedule(date.atTime(startTime), date.atTime(endTime), user, facilityId);
        model.addSchedule(schedule);
        loadSchedule(date); // Refresh the schedule list
    }
}
