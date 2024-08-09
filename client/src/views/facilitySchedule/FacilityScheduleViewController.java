package views.facilitySchedule;

import core.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import transferObjects.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<String> scheduleListView;

    @Override
    public void init() {

        try{
            scheduleViewModel = ViewModelFactory.getInstance().getFacilityScheduleVM();
        }
        catch (IOException | NotBoundException | SQLException e) {
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
        datePicker.property().bindBidirectional(scheduleViewModel.da)
        scheduleListView.property().bindBidirectional(scheduleViewModel.)
    }

    private void loadInitialSchedule(LocalDate date) {
        scheduleListView.getItems().clear();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        IntStream.range(6, 23).forEach(hour -> {
            String timeSlot = String.format("%02d:00 - %02d:00 FREE", hour, hour + 1);
            scheduleListView.getItems().add(timeSlot);
        });
    }

    private Callback<DatePicker, DateCell> getDayCellFactory() {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
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
        };
    }

    @FXML
    private void reserve() throws SQLException, IOException {
        if (datePicker.getValue() == null) {
            showAlert(AlertType.ERROR, "Form Error!", "Please select a date.");
            return;
        }
        viewModel.reserve();
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
