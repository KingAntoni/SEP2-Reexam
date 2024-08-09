package views.editFacility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.SportFacilityModel;
import transferObjects.Facility;

public class EditFacilityViewModel {

    private final StringProperty facilityName = new SimpleStringProperty();
    private final StringProperty facilityDescription = new SimpleStringProperty();
    private final SportFacilityModel model;
    private Facility currentFacility;

    public EditFacilityViewModel(SportFacilityModel model) {
        this.model = model;
    }

    public void loadFacility(Facility facility) {
        currentFacility = facility;
        facilityName.set(facility.getTitle());
        facilityDescription.set(facility.getDescription());
    }

    public StringProperty facilityNameProperty() {
        return facilityName;
    }

    public StringProperty facilityDescriptionProperty() {
        return facilityDescription;
    }

    public void updateFacility() {
        if (currentFacility != null) {
            currentFacility.setTitle(facilityName.get());
            currentFacility.setDescription(facilityDescription.get());
            try {
                model.editFacility(currentFacility);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
