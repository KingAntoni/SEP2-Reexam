package views.addFacility;

import core.ViewHandler;
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

    private ViewHandler viewHandler;

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

    public void init(ViewModelFactory instance, ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }


    @FXML
    private void createFacility() {
        try {
            if (facilityViewModel.getFacilityName().isEmpty() || facilityViewModel.getFacilityDescription().isEmpty()) {
                showAlert(AlertType.ERROR, "Form Error!", "Please enter facility name and description");
                return;
            }
            facilityViewModel.createFacility();
            viewHandler.openFacilityMenu();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Creation Error", "Error creating facility: " + e.getMessage());
        }
    }

    @FXML
    public void backButtonPressed() {
        viewHandler.openFacilityMenu();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
