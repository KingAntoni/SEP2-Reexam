package views.facilityScheduleAdmin;

import core.ViewModelFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
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
    private ComboBox<String> facilityComboBox;

    @Override
    public void init() {
        try {
            scheduleViewModel = ViewModelFactory.getInstance().getFacilityScheduleAdminVM();
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Initialization Error", "Error initializing ViewModel: " + e.getMessage());
            return;
        }

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
            showAlert(AlertType.ERROR, "Form Error!", "Please select a date.");
            return;
        }
        scheduleViewModel.reserve();
        showAlert(AlertType.INFORMATION, "Reservation Successful", "Your reservation has been made.");
        loadInitialSchedule(datePicker.getValue()); // Refresh the schedule list
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
