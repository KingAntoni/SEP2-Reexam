package views.editFacility;

import core.ViewHandler;
import core.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import transferObjects.Facility;
import views.ViewController;

import java.io.IOException;
import java.sql.SQLException;

public class EditFacilityViewController implements ViewController {

    @FXML
    private TextField facilityNameField;

    @FXML
    private TextField facilityDescriptionField;

    private EditFacilityViewModel editFacilityViewModel;
    private ViewHandler viewHandler;

    @Override
    public void init() {
    }

    public void init(ViewModelFactory viewModelFactory, ViewHandler viewHandler, Facility facility) throws IOException, SQLException {
        this.viewHandler = viewHandler;
        this.editFacilityViewModel = viewModelFactory.getEditFacilityVM();
        this.editFacilityViewModel.loadFacility(facility);

        facilityNameField.textProperty().bindBidirectional(editFacilityViewModel.facilityNameProperty());
        facilityDescriptionField.textProperty().bindBidirectional(editFacilityViewModel.facilityDescriptionProperty());
    }

    @FXML
    public void updateFacility() {
        editFacilityViewModel.updateFacility();
        viewHandler.openFacilityMenu();
    }

    @FXML
    public void backButtonPressed() {
        viewHandler.openFacilityMenu();
    }
}
