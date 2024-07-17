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
        addButtonToMenu("Géstion des véhicule", "fas-car", this::handleAddVehicle);
        addButtonToMenu("Gérer les trajets", "fas-route", this::handleManageTrips);
        addButtonToMenu("Gérer les utilisateurs", "fas-users", this::handleManageUsers);
        addButtonToMenu("Se déconnecter", "fas-sign-out-alt", this::handleLogout);

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
        loadView("/com/groupeisi/rent/user/userMenu.fxml");
    }

    @FXML
    private void handleLogout() {
        ViewUtils.switchToView("/com/groupeisi/rent/user/login.fxml", (Stage) menuBox.getScene().getWindow());
    }

    @FXML
    private void handleAddVehicle() {
        loadView("/com/groupeisi/rent/vehicle/Add_vehicle.fxml");
    }

    @FXML
    private void handleManageTrips() {
        loadView("/com/groupeisi/rent/trip/trip.fxml");
    }

    @FXML
    private void handleManageUsers() {
        loadView("/com/groupeisi/rent/user/manageUsers.fxml");
    }

}