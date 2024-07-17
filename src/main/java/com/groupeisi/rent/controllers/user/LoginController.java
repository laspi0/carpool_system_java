package com.groupeisi.rent.controllers.user;

import com.groupeisi.rent.Config.SessionManager;
import com.groupeisi.rent.entities.User;
import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.Config.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private void initialize() {
        registerLink.setOnAction(event -> goToRegisterPage());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            showAlert("Error", "Incorrect email or password.");
        } else {
            SessionManager.setLoggedInUser(user);
            showAlert("Success", "Login successful.");

            Stage currentStage = (Stage) emailField.getScene().getWindow();
            loadDashboard(user.getRole(), currentStage);

            clearFields();
        }
    }

    private void loadDashboard(String role, Stage currentStage) {
        String fxmlPath = "";
        if (role.equals("Admin")) {
            fxmlPath = "/com/groupeisi/rent/user/adminDashboard.fxml";
        } else {
            fxmlPath = "/com/groupeisi/rent/user/userDashboard.fxml";
        }

        ViewUtils.switchToView(fxmlPath, currentStage);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
    }

    private void goToRegisterPage() {
        Stage currentStage = (Stage) registerLink.getScene().getWindow();
        String fxmlPath = "/com/groupeisi/rent/user/register.fxml";

        ViewUtils.switchToView(fxmlPath, currentStage);
    }
}