package views.facilityMenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.SportFacilityModel;
import transferObjects.Facility;

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

    public void loadFacilities() {
        facilities.clear();
        try {
            facilities.addAll(model.getAllFacilities());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFacility(Facility facility) {
        try {
            model.deleteFacility(facility);
            loadFacilities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
