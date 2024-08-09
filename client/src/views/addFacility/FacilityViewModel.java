package views.addFacility;

import model.SportFacilityModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import transferObjects.Facility;

import java.io.IOException;
import java.sql.SQLException;

public class FacilityViewModel {
    private final StringProperty facilityName = new SimpleStringProperty();
    private final StringProperty facilityDescription = new SimpleStringProperty();
    private final SportFacilityModel model;

    public FacilityViewModel(SportFacilityModel model) {
        this.model = model;
    }

    public StringProperty facilityNameProperty() {
        return facilityName;
    }

    public String getFacilityName() {
        return facilityName.get();
    }

    public StringProperty facilityDescriptionProperty() {
        return facilityDescription;
    }

    public String getFacilityDescription() {
        return facilityDescription.get();
    }

    public void createFacility() throws IOException, SQLException {
        model.createFacility(new Facility(getFacilityName(), getFacilityDescription()));
        System.out.println("Facility " + getFacilityName() + " created with description: " + getFacilityDescription());
    }
}
