package views.login;

import core.ViewHandler;
import core.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import views.ViewController;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class LoginViewController implements ViewController {
    private LoginViewModel loginViewModel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private Label errorLabel;

    @Override
    public void init() {
        try {
            loginViewModel = ViewModelFactory.getInstance().getLoginVM();
            if (loginViewModel == null) {
                throw new IllegalStateException("LoginViewModel is null");
            }
            usernameField.textProperty().bindBidirectional(loginViewModel.usernameProperty());
            passwordField.textProperty().bindBidirectional(loginViewModel.passwordProperty());
            adminCheckBox.selectedProperty().bindBidirectional(loginViewModel.adminProperty());
            errorLabel.textProperty().bind(loginViewModel.errorLabelProperty());  // Bind the error label
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Initialization Error", "Error initializing ViewModel: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() throws SQLException, IOException {
        boolean success = loginViewModel.logIn();
        if (success) {
            // Close the login view and open the facility schedule view
            if(!adminCheckBox.isSelected()) {
                ViewHandler.getInstance().openFacilityMenuUser();
            }
            else {
                ViewHandler.getInstance().openFacilityMenu();
            }
        } else {
            // Optionally, show an error alert if needed (handled by errorLabel in the UI)
            showAlert(AlertType.ERROR, "Login Failed", "Incorrect username, password, or admin status.");
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
