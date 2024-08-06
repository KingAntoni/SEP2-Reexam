package views.addFacility;

import core.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import views.ViewController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class FacilityViewController implements ViewController {

    private FacilityViewModel facilityViewModel;

    @FXML
    private TextField facilityNameField;

    @FXML
    private TextField facilityDescriptionField;

    @FXML
    @Override
    public void init() {
        try {
            facilityViewModel = ViewModelFactory.getInstance().getFacilityVM();
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Initialization Error", "Error initializing ViewModel: " + e.getMessage());
            return;
        }
        facilityNameField.textProperty().bindBidirectional(facilityViewModel.facilityNameProperty());
        facilityDescriptionField.textProperty().bindBidirectional(facilityViewModel.facilityDescriptionProperty());
    }

    @FXML
    private void createFacility() {
        try {
            if (facilityViewModel.getFacilityName().isEmpty() || facilityViewModel.getFacilityDescription().isEmpty()) {
                showAlert(AlertType.ERROR, "Form Error!", "Please enter facility name and description");
                return;
            }
            facilityViewModel.createFacility();
            showAlert(AlertType.INFORMATION, "Facility Created!", "Facility " + facilityViewModel.getFacilityName() + " has been created with description: " + facilityViewModel.getFacilityDescription());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Creation Error", "Error creating facility: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
