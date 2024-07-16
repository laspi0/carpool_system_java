package com.groupeisi.rent.controllers.user;

import com.groupeisi.rent.Config.ViewUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import java.io.IOException;
import javafx.application.Platform;

public class UserDashboardController {

    @FXML
    private VBox menuBox;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        System.out.println("Initialize method called in UserDashboardController");
        System.out.println("menuBox: " + (menuBox != null ? "not null" : "null"));
        System.out.println("contentArea: " + (contentArea != null ? "not null" : "null"));

        // Utilisation de Platform.runLater pour s'assurer que l'interface utilisateur est complètement chargée
        Platform.runLater(() -> {
            System.out.println("Setting up menu");
            setupMenu();
        });
    }

    private void setupMenu() {
        if (menuBox != null) {
            addButtonToMenu("Tableau de bord", "fas-tachometer-alt", this::handleDashboard);
            addButtonToMenu("Mes réservations", "fas-calendar-check", this::handleMyReservations);
            addButtonToMenu("Voir les trajets", "fas-route", this::handleViewTrips);
            addButtonToMenu("Se déconnecter", "fas-sign-out-alt", this::handleLogout);

            // Afficher le tableau de bord par défaut
            handleDashboard();
        } else {
            System.err.println("menuBox is null in setupMenu method");
        }
    }

    private void addButtonToMenu(String text, String icon, Runnable action) {
        if (menuBox != null) {
            Button button = new Button(text);
            button.getStyleClass().add("menu-button");
            button.setGraphic(new FontIcon(icon));
            button.setOnAction(event -> {
                System.out.println("Button clicked: " + text); // Log pour le débogage
                action.run();
            });
            menuBox.getChildren().add(button);
        } else {
            System.err.println("Cannot add button '" + text + "' because menuBox is null");
        }
    }

    private void loadView(String fxmlPath) {
        System.out.println("Loading view: " + fxmlPath); // Log pour le débogage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (contentArea != null) {
                contentArea.getChildren().clear();
                Parent view = loader.load();
                contentArea.getChildren().add(view);
                System.out.println("View loaded successfully"); // Log pour le débogage
            } else {
                System.err.println("contentArea is null in loadView method");
            }
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlPath); // Log pour le débogage
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard() {
        loadView("/com/groupeisi/rent/user/userMenu.fxml");
    }

    @FXML
    private void handleMyReservations() {
        loadView("/com/groupeisi/rent/reservation/myReservations.fxml");
    }

    @FXML
    private void handleViewTrips() {
        loadView("/com/groupeisi/rent/trip/viewTrips.fxml");
    }

    @FXML
    private void handleLogout() {
        if (menuBox != null && menuBox.getScene() != null && menuBox.getScene().getWindow() != null) {
            ViewUtils.switchToView("/com/groupeisi/rent/user/login.fxml", (Stage) menuBox.getScene().getWindow());
        } else {
            System.err.println("Unable to get the Stage in handleLogout method");
        }
    }
}