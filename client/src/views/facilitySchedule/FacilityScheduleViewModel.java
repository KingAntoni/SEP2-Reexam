package views.facilitySchedule;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.SportFacilityModel;
import transferObjects.Facility;
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
    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> selectedHour = new SimpleObjectProperty<>();
    private final ListProperty<String> scheduleList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private Facility currentFacility;
    private final ObjectProperty<String> selectedTimeSlot = new SimpleObjectProperty<>();


    public FacilityScheduleViewModel(SportFacilityModel model) {
        this.model = model;
    }

    public void loadFacility(Facility facility) {
        currentFacility = facility;
        model.setFacilityId(facility.getId()); // Ensure the facility ID is set in the model
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return selectedDate;
    }

    public ListProperty<String> scheduleListProperty() {
        return scheduleList;
    }


    public void loadSchedule(LocalDate date) {
        scheduleList.clear();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<Schedule> schedules = model.getSchedulesForDate(date);
        for (int hour = 6; hour < 23; hour++) {
            LocalTime startTime = LocalTime.of(hour, 0);
            LocalTime endTime = startTime.plusHours(1);
            String status = schedules.stream().anyMatch(s -> s.getStartTime().toLocalTime().equals(startTime)) ? "RESERVED" : "FREE";
            scheduleList.add(startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter) + " " + status);
        }
    }

    public void setSelectedTimeSlot(String timeSlot) {
        selectedTimeSlot.set(timeSlot);
        // Extract the start time from the selected time slot
        if (timeSlot != null && !timeSlot.isEmpty()) {
            String startTimeStr = timeSlot.split(" - ")[0];
            LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
            selectedHour.set(startTime);
        }
    }

    public void reserve() throws SQLException, IOException {
        LocalDate date = selectedDate.get();
        LocalTime startTime = selectedHour.get();
        if (date == null || startTime == null) {
            throw new IllegalStateException("Date or time not selected");
        }

        LocalTime endTime = startTime.plusHours(1);
        Schedule schedule = new Schedule(date.atTime(startTime), date.atTime(endTime), null, currentFacility.getId());
        model.reserveFacility(schedule);
        loadSchedule(date); // Refresh the schedule list
    }
}
