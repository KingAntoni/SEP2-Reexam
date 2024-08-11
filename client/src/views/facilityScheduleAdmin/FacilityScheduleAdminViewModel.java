package views.facilityScheduleAdmin;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class FacilityScheduleAdminViewModel {
    private final SportFacilityModel model;
    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> selectedHour = new SimpleObjectProperty<>();
    private final ListProperty<String> scheduleList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private Facility currentFacility;
    private final ObservableList<String> users;
    private final ObjectProperty<String> selectedTimeSlot = new SimpleObjectProperty<>();
    private final ObjectProperty<String> selectedUser = new SimpleObjectProperty<>();

    public FacilityScheduleAdminViewModel(SportFacilityModel model) {
        this.model = model;
        this.users = FXCollections.observableArrayList();
        loadUsers();
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

    public ObservableList<String> getUsers(){
        return users;
    }

    public void loadUsers() {
        users.clear();
        try {
            List<User> userInstances = model.getAllUsers();
            for (User user : userInstances) {
                users.add(user.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void setSelectedUser(String username) {
        selectedUser.set(username);
    }

    public void reserve() throws SQLException, IOException {
        LocalDate date = selectedDate.get();
        LocalTime startTime = selectedHour.get();
        String username = selectedUser.get();
        if (date == null || startTime == null || username == null) {
            throw new IllegalStateException("Date, time, or user not selected");
        }

        LocalTime endTime = startTime.plusHours(1);
        User user = new User(username,null,false);
        Schedule schedule = new Schedule(date.atTime(startTime), date.atTime(endTime), user, currentFacility.getId());
        model.reserveFacility(schedule);
        loadSchedule(date); // Refresh the schedule list after reservation
    }
}

