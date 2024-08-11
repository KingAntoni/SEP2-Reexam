package views.facilityScheduleAdmin;

import core.ViewHandler;
import core.ViewModelFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import transferObjects.Facility;
import views.ViewController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class FacilityScheduleAdminViewController implements ViewController {
    private FacilityScheduleAdminViewModel scheduleViewModel;
    private final ObjectProperty<LocalTime> selectedHour = new SimpleObjectProperty<>();

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<String> scheduleListView;

    @FXML
    private ComboBox<String> userComboBox;

    private ViewHandler viewHandler;

    @Override
    public void init() {
    }

    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler, Facility facility) {
        this.viewHandler = viewHandler;

        // Initialize the ViewModel
        scheduleViewModel = viewModelFactory.getFacilityScheduleAdminVM();

        if (scheduleViewModel == null) {
            throw new IllegalStateException("FacilityScheduleAdminViewModel is not available from ViewModelFactory.");
        }

        // Load users
        userComboBox.setItems(
                scheduleViewModel.getUsers()
        );

        // Load the facility and set up the rest of the view
        scheduleViewModel.loadFacility(facility);
        datePicker.setDayCellFactory(getDayCellFactory());
        datePicker.setValue(LocalDate.now());
        loadInitialSchedule(LocalDate.now());

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                loadInitialSchedule(newDate);
            }
        });

        // Bind properties to the ViewModel
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
        if (userComboBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        // Set the selected time and user to the ViewModel
        String selectedTimeSlot = scheduleListView.getSelectionModel().getSelectedItem();
        String selectedUser = userComboBox.getSelectionModel().getSelectedItem();
        scheduleViewModel.setSelectedTimeSlot(selectedTimeSlot);
        scheduleViewModel.setSelectedUser(selectedUser);

        // Attempt to reserve the facility
        scheduleViewModel.reserve();
        loadInitialSchedule(datePicker.getValue()); // Refresh the schedule list
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        String[] timeSlotParts = selectedTimeSlot.split(" ");
        String username = timeSlotParts[5];

        boolean success = scheduleViewModel.cancelReservation(username);
        if (success) {
            loadInitialSchedule(datePicker.getValue()); // Refresh the schedule list
        } else {
            showAlert(AlertType.ERROR, "Release Failed", "Failed to release the reservation. Please try again.");
        }
    }

    @FXML
    public void backButtonPressed() {
        viewHandler.openFacilityMenu();
    }
}
