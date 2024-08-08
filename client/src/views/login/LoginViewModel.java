package views.login;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel {
    private final StringProperty username;
    private final StringProperty password;

    public LoginViewModel() {
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public boolean validateCredentials(String username, String password) {
        // Replace with actual authentication logic
        return "user".equals(username) && "pass".equals(password);
    }
}
