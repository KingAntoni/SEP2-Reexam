package views.login;

import core.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import views.ViewController;
import core.ViewHandler;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class LoginViewController implements ViewController {
    private LoginViewModel loginviewModel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox adminCheckBox;

    @Override
    public void init() {
        try {
            loginviewModel = ViewModelFactory.getInstance().getLoginVM();
        } catch (IOException | NotBoundException | SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Initialization Error", "Error initializing ViewModel: " + e.getMessage());
            return;
        }
        usernameField.textProperty().bindBidirectional(loginviewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(loginviewModel.passwordProperty());
    }

    @FXML
    private void handleLogin() throws SQLException, IOException {
        loginviewModel.logIn();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
