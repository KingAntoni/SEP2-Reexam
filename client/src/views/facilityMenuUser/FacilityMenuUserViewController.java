package views.facilityMenuUser;

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

public class FacilityMenuUserViewController implements ViewController {

    private FacilityMenuUserViewModel facilityMenuViewModel;
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
            facilityMenuUserViewModel = ViewModelFactory.getInstance().getFacilityMenuVM();
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
    public void reserveFacilityButton(ActionEvent actionEvent) {
        Facility selectedFacility = facilityTableView.getSelectionModel().getSelectedItem();
        if (selectedFacility != null) {
            // Add your implementation for reserving the facility
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
