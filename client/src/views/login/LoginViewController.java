package views.login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import views.ViewController;

public class LoginViewController implements ViewController {
    private LoginViewModel viewModel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void init() {
        // Controller initialization without ViewModel if needed
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (viewModel.validateCredentials(username, password)) {
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
