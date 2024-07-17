package com.groupeisi.rent.controllers.user;

import com.groupeisi.rent.Config.ViewUtils;
import com.groupeisi.rent.entities.User;
import com.groupeisi.rent.DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void handleRegister() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        User user = new User(firstName, lastName, email, password, role);
        UserDAO userDAO = new UserDAO();
        userDAO.save(user);

        showAlert("Succès", "Inscription réussie.");
        clearFields();
    }

    @FXML
    private void goToLoginPage() {
        Stage currentStage = (Stage) firstNameField.getScene().getWindow();
        ViewUtils.switchToView("/com/groupeisi/rent/user/login.fxml", currentStage);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}
