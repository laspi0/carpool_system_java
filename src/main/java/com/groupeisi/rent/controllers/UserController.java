package com.groupeisi.rent.controllers;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.entities.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserController {
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

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "User", "Manager"));
    }

    @FXML
    private void handleSave() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
                showAlert("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            User user = new User(firstName, lastName, email, password, role);
            userDAO.save(user);

            showAlert("Succès", "L'utilisateur a été enregistré avec succès.");
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'enregistrement : " + e.getMessage());
            e.printStackTrace();
        }
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
