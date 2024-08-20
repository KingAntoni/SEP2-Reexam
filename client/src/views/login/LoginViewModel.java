package views.login;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.SportFacilityModel;
import network.Client;
import transferObjects.User;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class LoginViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password =  new SimpleStringProperty();
    private final StringProperty errorLabel = new SimpleStringProperty();
    private final BooleanProperty admin = new SimpleBooleanProperty();
    private final SportFacilityModel model;

    public LoginViewModel(SportFacilityModel model) {
        this.model = model;
    }

    public boolean logIn() throws IOException, SQLException {
        if (username.getValue() == null || password.getValue() == null) {
            errorLabel.setValue("Type in your username and password");
            return false;
        } else if (!model.logIn(username.getValue(), password.getValue(), admin.getValue())) {
            errorLabel.setValue("Wrong password or username");
            return false;
        } else {
            return true; // Login successful
        }
    }


    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public BooleanProperty adminProperty() {
        return admin;
    }

    public StringProperty errorLabelProperty() {
        return errorLabel;
    }
}
