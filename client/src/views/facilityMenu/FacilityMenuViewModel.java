package views.facilityMenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.SportFacilityModel;
import transferObjects.Facility;

import java.io.IOException;
import java.sql.SQLException;

public class FacilityMenuViewModel {

    private final SportFacilityModel model;
    private final ObservableList<Facility> facilities;

    public FacilityMenuViewModel(SportFacilityModel model) {
        this.model = model;
        this.facilities = FXCollections.observableArrayList();
        loadFacilities();
    }

    public ObservableList<Facility> getFacilities() {
        return facilities;
    }

    private void loadFacilities() {
        try {
            facilities.setAll(model.getAllFacilities());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFacility(Facility facility) throws IOException, SQLException {
        if (model.deleteFacility(facility)) {
            facilities.remove(facility);
        }
    }
}
