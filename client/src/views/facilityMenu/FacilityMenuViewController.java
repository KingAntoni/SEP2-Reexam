package views.Menu;

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
import views.facilityMenu.FacilityMenuViewModel;

import java.io.IOException;
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
        // This method will be called by the FXMLLoader
    }

    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler) throws IOException, SQLException {
        this.viewHandler = viewHandler;
        facilityMenuViewModel = viewModelFactory.getFacilityMenuVM();
        bindTableColumns();
        facilityTableView.setItems(facilityMenuViewModel.getFacilities());
    }

    private void bindTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    @FXML
    public void addFacilityButton(ActionEvent actionEvent) {
        viewHandler.openCreateFacility();
    }

    @FXML
    public void editFacilityButton(ActionEvent actionEvent) {
        Facility selectedFacility = facilityTableView.getSelectionModel().getSelectedItem();
        if (selectedFacility != null) {
            // Implement the openEditFacility method in ViewHandler
            // viewHandler.openEditFacility(selectedFacility);
        }
    }

    @FXML
    public void deleteFacilityButton(ActionEvent actionEvent) {
        Facility selectedFacility = facilityTableView.getSelectionModel().getSelectedItem();
        if (selectedFacility != null) {
            try {
                facilityMenuViewModel.deleteFacility(selectedFacility);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Deletion Error", "Error deleting facility: " + e.getMessage());
            }
        }
    }

    @FXML
    public void reserveFacilityButton(ActionEvent actionEvent) {
        Facility selectedFacility = facilityTableView.getSelectionModel().getSelectedItem();
        if (selectedFacility != null) {
            // Implement the openReserveFacility method in ViewHandler
            // viewHandler.openReserveFacility(selectedFacility);
        }
    }

    @FXML
    public void logOutButtonPressed(ActionEvent actionEvent) {
        // Implement the openLogOut method in ViewHandler
        // viewHandler.openLogOut();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
