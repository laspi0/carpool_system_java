package com.groupeisi.rent.controllers.user;

import com.groupeisi.rent.Config.ViewUtils;
import com.groupeisi.rent.entities.User;
import com.groupeisi.rent.DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

        // Vérifiez si les champs ne sont pas vides
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifiez les informations de connexion
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            showAlert("Erreur", "Email ou mot de passe incorrect.");
        } else {
            showAlert("Succès", "Connexion réussie.");
            clearFields();
            // Rediriger vers une autre vue après la connexion réussie si nécessaire
        }
    }

    private void goToRegisterPage() {
        Stage currentStage = (Stage) registerLink.getScene().getWindow();
        // Remplacez "path_to_register.fxml" par le chemin réel vers votre fichier FXML de la page d'inscription
        ViewUtils.switchToView("/com/groupeisi/rent/user/register.fxml", currentStage);
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
}
