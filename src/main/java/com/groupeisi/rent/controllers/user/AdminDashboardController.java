package com.groupeisi.rent.controllers.user;

import com.groupeisi.rent.Config.ViewUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private VBox menuBox;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        addButtonToMenu("Tableau de bord", "fas-tachometer-alt", this::handleDashboard);
        addButtonToMenu("Ajouter un véhicule", "fas-car", this::handleAddVehicle); // Bouton pour ajouter un véhicule
        addButtonToMenu("Gérer les utilisateurs", "fas-users", this::handleManageUsers);
        addButtonToMenu("Gérer les propriétés", "fas-home", this::handleManageProperties);
        addButtonToMenu("Se déconnecter", "fas-sign-out-alt", this::handleLogout);

        // Afficher le tableau de bord par défaut
        handleDashboard();
    }

    private void addButtonToMenu(String text, String icon, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        button.setGraphic(new FontIcon(icon));
        button.setOnAction(event -> action.run());
        menuBox.getChildren().add(button);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard() {
        loadView("/com/groupeisi/rent/user/dashboard.fxml");
    }

    @FXML
    private void handleLogout() {
        ViewUtils.switchToView("/com/groupeisi/rent/user/login.fxml", (Stage) menuBox.getScene().getWindow());
    }

    @FXML
    private void handleAddVehicle() {
        loadView("/com/groupeisi/rent/vehicle/Add_vehicle.fxml"); // Charger la vue pour ajouter un véhicule
    }

    @FXML
    private void handleManageUsers() {
        loadView("/com/groupeisi/rent/user/manageUsers.fxml");
    }

    @FXML
    private void handleManageProperties() {
        loadView("/com/groupeisi/rent/user/manageProperties.fxml");
    }
}
