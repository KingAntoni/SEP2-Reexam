package views.login;

import core.ViewHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.SportFacilityModel;
import network.Client;
import transferObjects.User;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginViewModel {
    private final StringProperty username;
    private final StringProperty password;
    private StringProperty errorLabel;
    private Client client;
    private ViewHandler viewHandler;
    private SportFacilityModel sportFacilityModel;

    public LoginViewModel() {
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            client = (Client) registry.lookup("Server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logIn() throws IOException, SQLException {
        if(username.getValue()==null|| password.getValue()==null)
        {
            errorLabel.setValue("Type in your username and password");
        }
        else if(!sportFacilityModel.logIn(username.getValue(), password.getValue()))
        {
            errorLabel.setValue("Wrong password or username");
        }
        else
        {
            viewHandler.openFacilitySchedule();
        }
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public boolean validateCredentials(String username, String password, boolean isAdmin) {
        try {
            User user = new User(username, password, isAdmin);
            return client.login(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
