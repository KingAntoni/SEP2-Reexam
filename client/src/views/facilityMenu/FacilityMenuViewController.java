package views.facilityMenu;

import core.ViewHandler;
import core.ViewModelFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transferObjects.Facility;
import views.ViewController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class FacilityMenuViewController implements ViewController {

    private FacilityMenuViewModel facilityMenuViewModel;
    private ViewHandler viewHandler;

    @FXML
    private TableView<Facility> facilityTableView;

    @FXML
    private TableColumn<Facility, String> nameColumn;

    @FXML
    private TableColumn<Facility, String> descriptionColumn;

    @Override
    public void init() {
        try {
            facilityMenuViewModel = ViewModelFactory.getInstance().getFacilityMenuVM();
            bindTableColumns();
            facilityTableView.setItems(facilityMenuViewModel.getFacilities());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Error initializing ViewModel: " + e.getMessage());
        }
    }

    private void bindTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    public void addFacilityButton(ActionEvent actionEvent) {
        viewHandler.getInstance().openCreateFacility();
    }

    @FXML
    public void editFacilityButton(ActionEvent actionEvent) throws IOException, NotBoundException, SQLException {
        Facility selectedFacility = facilityTableView.getSelectionModel().getSelectedItem();
        if (selectedFacility != null) {
            viewHandler.getInstance().openEditFacility(selectedFacility);
        }
    }
    @FXML
    public void deleteFacilityButton(ActionEvent actionEvent) {
        Facility selectedFacility = facilityTableView.getSelectionModel().getSelectedItem();
        if (selectedFacility != null) {
            try {
                facilityMenuViewModel.deleteFacility(selectedFacility);
                // Refresh the TableView data
                facilityTableView.setItems(facilityMenuViewModel.getFacilities());
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Deletion Error", "Error deleting facility: " + e.getMessage());
            }
        } else {
            // Show a warning if no facility is selected
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a facility to delete.");
        }
    }


    @FXML
    public void reserveFacilityButton(ActionEvent actionEvent) {
        Facility selectedFacility = facilityTableView.getSelectionModel().getSelectedItem();
        if (selectedFacility != null) {
            // Add your implementation for reserving the facility
            facilityMenuViewModel.setFacilityId(selectedFacility.getId());
        }
    }

    @FXML
    public void logOutButtonPressed(ActionEvent actionEvent) {
        // Add your implementation for logout
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
