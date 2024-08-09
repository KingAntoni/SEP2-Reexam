package views.facilitySchedule;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.SportFacilityModel;
import transferObjects.Schedule;
import transferObjects.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FacilityScheduleViewModel {
    private final SportFacilityModel model;
    private final StringProperty selectedDate = new SimpleStringProperty();
    private final ObjectProperty<LocalTime> selectedHour = new SimpleObjectProperty<>();
    private final ListProperty<String> scheduleList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private int facilityId;

    public FacilityScheduleViewModel(SportFacilityModel model) {
        this.model = model;
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
                new Schedule(date.atTime(10, 0), date.atTime(11, 0), new User("reservedUser", "password", false), facilityId)
        );
        for (int hour = 6; hour < 23; hour++) {
            LocalTime startTime = LocalTime.of(hour, 0);
            LocalTime endTime = startTime.plusHours(1);
            String status = schedules.stream().anyMatch(s -> s.getStartTime().equals(startTime)) ? "RESERVED" : "FREE";
            scheduleList.add(startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter) + " " + status);
        }
    }

    public void reserve() throws SQLException, IOException {
        LocalDate date = LocalDate.parse(selectedDate.get());
        LocalTime startTime = selectedHour.get();
        LocalTime endTime = startTime.plusHours(1); // Assuming fixed 1-hour slots
        Schedule schedule = new Schedule(date.atTime(startTime), date.atTime(endTime), null, facilityId);
        model.reserveFacility(schedule);
        loadSchedule(date); // Refresh the schedule list
    }
}
