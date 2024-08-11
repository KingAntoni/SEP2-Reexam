package views.facilitySchedule;

import core.ViewHandler;
import core.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import transferObjects.Facility;
import views.ViewController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;
import javafx.util.Callback;
import javafx.scene.control.DateCell;

public class FacilityScheduleViewController implements ViewController {
    private FacilityScheduleViewModel scheduleViewModel;
    private final ObjectProperty<LocalTime> selectedHour = new SimpleObjectProperty<>();
    private ViewHandler viewHandler;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<String> scheduleListView;

    @Override
    public void init() {

    }

    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler, Facility facility){
        this.viewHandler = viewHandler;
        scheduleViewModel = viewModelFactory.getFacilityScheduleVM();
        this.scheduleViewModel.loadFacility(facility);

        datePicker.setDayCellFactory(getDayCellFactory());
        datePicker.setValue(LocalDate.now());
        loadInitialSchedule(LocalDate.now());

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadInitialSchedule(newDate);
            }
        });

        // Bind the DatePicker and ListView to the ViewModel
        datePicker.valueProperty().bindBidirectional(scheduleViewModel.dateProperty());
        scheduleListView.itemsProperty().bind(scheduleViewModel.scheduleListProperty());
    }

    private void loadInitialSchedule(LocalDate date) {
        scheduleViewModel.loadSchedule(date);
    }

    private Callback<DatePicker, DateCell> getDayCellFactory() {
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
    }

    @FXML
    private void reserve() throws SQLException, IOException {
        if (datePicker.getValue() == null) {
            return;
        }
        if (scheduleListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        String selectedTimeSlot = scheduleListView.getSelectionModel().getSelectedItem();
        scheduleViewModel.setSelectedTimeSlot(selectedTimeSlot);

        scheduleViewModel.reserve();
        loadInitialSchedule(datePicker.getValue());
    }

    @FXML
    public void releaseButtonPressed() throws IOException, SQLException {
        if (datePicker.getValue() == null) {
            showAlert(AlertType.WARNING, "No Date Selected", "Please select a date to release a reservation.");
            return;
        }

        if (scheduleListView.getSelectionModel().getSelectedItem() == null) {
            showAlert(AlertType.WARNING, "No Time Slot Selected", "Please select a time slot to release the reservation.");
            return;
        }

        String selectedTimeSlot = scheduleListView.getSelectionModel().getSelectedItem();
        scheduleViewModel.setSelectedTimeSlot(selectedTimeSlot);

        // Extract the username from the selected time slot
        String[] timeSlotParts = selectedTimeSlot.split(" ");
        if (timeSlotParts.length < 5 || !"RESERVED".equals(timeSlotParts[3])) {
            showAlert(AlertType.WARNING, "Not Reserved", "The selected time slot is not reserved.");
            return;
        }

        String username = timeSlotParts[5];

        if (!scheduleViewModel.checkUsernameMatch(username)) {
            showAlert(AlertType.ERROR, "User Mismatch", "You cannot release a reservation made by another user.");
            return;
        }

        // Cancel the reservation
        boolean success = scheduleViewModel.cancelReservation();
        if (success) {
            loadInitialSchedule(datePicker.getValue()); // Refresh the schedule list
        } else {
            showAlert(AlertType.ERROR, "Release Failed", "Failed to release the reservation. Please try again.");
        }
    }


    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void backButtonPressed() throws IOException {
        viewHandler.openFacilityMenuUser();
    }
}
