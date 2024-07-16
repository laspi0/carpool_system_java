package com.groupeisi.rent.controllers.vehicle;

import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.DAO.VehicleDAO;
import com.groupeisi.rent.entities.User;
import com.groupeisi.rent.entities.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

    @FXML
    private VBox inputVBox;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField registrationField;
    @FXML
    private ComboBox<User> driverComboBox;
    @FXML
    private TableView<Vehicle> vehicleTableView;
    @FXML
    private TableColumn<Vehicle, String> brandColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, String> registrationColumn;
    @FXML
    private TableColumn<Vehicle, User> driverColumn;

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final UserDAO userDAO = new UserDAO();
    private Vehicle selectedVehicle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableView();
        loadDrivers();
        setupTableViewSelection();
        createButtons();
    }

    private void initializeTableView() {
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        registrationColumn.setCellValueFactory(new PropertyValueFactory<>("registration"));
        driverColumn.setCellValueFactory(new PropertyValueFactory<>("driver"));

        driverColumn.setCellFactory(column -> new TableCell<Vehicle, User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getFirstName() + " " + user.getLastName());
                }
            }
        });

        refreshTableView();
    }

    private void loadDrivers() {
        List<User> drivers = userDAO.findDrivers();
        driverComboBox.setItems(FXCollections.observableArrayList(drivers));
        driverComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user == null ? "" : user.getFirstName() + " " + user.getLastName();
            }

            @Override
            public User fromString(String string) {
                return null; // Not used
            }
        });
    }

    private void setupTableViewSelection() {
        vehicleTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedVehicle = newSelection;
                brandField.setText(newSelection.getBrand());
                modelField.setText(newSelection.getModel());
                registrationField.setText(newSelection.getRegistration());
                driverComboBox.setValue(newSelection.getDriver());
            }
        });
    }

    private void createButtons() {
        Label addLabel = new Label("Ajouter");
        addLabel.setOnMouseClicked(event -> addVehicle());

        Label editLabel = new Label("Modifier");
        editLabel.setOnMouseClicked(event -> updateVehicle());

        Label deleteLabel = new Label("Supprimer");
        deleteLabel.setOnMouseClicked(event -> deleteVehicle());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(addLabel, editLabel, deleteLabel);

        // Style pour les labels
        String labelStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 15 8 15; -fx-cursor: hand;";
        addLabel.setStyle(labelStyle);
        editLabel.setStyle(labelStyle);
        deleteLabel.setStyle(labelStyle);

        inputVBox.getChildren().add(buttonContainer);
    }

    private void addVehicle() {
        if (validateInput()) {
            Vehicle newVehicle = createVehicleFromInput();
            vehicleDAO.save(newVehicle);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Véhicule ajouté", "Le véhicule a été ajouté avec succès.");
            clearFields();
            refreshTableView();
        }
    }

    private void updateVehicle() {
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun véhicule sélectionné", "Veuillez sélectionner un véhicule à modifier.");
            return;
        }

        if (validateInput()) {
            updateSelectedVehicle();
            vehicleDAO.update(selectedVehicle);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Véhicule modifié", "Le véhicule a été modifié avec succès.");
            clearFields();
            refreshTableView();
        }
    }

    private void deleteVehicle() {
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Aucun véhicule sélectionné", "Veuillez sélectionner un véhicule à supprimer.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Supprimer le véhicule");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce véhicule ?\n\n" +
                "Marque : " + selectedVehicle.getBrand() + "\n" +
                "Modèle : " + selectedVehicle.getModel() + "\n" +
                "Immatriculation : " + selectedVehicle.getRegistration());

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    vehicleDAO.delete(selectedVehicle);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Véhicule supprimé", "Le véhicule a été supprimé avec succès.");
                    clearFields();
                    refreshTableView();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression", "Une erreur est survenue lors de la suppression du véhicule : " + e.getMessage());
                }
            }
        });
    }

    private boolean validateInput() {
        if (brandField.getText().trim().isEmpty() || modelField.getText().trim().isEmpty() ||
                registrationField.getText().trim().isEmpty() || driverComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Champs incomplets", "Tous les champs doivent être remplis.");
            return false;
        }
        return true;
    }

    private Vehicle createVehicleFromInput() {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(brandField.getText().trim());
        vehicle.setModel(modelField.getText().trim());
        vehicle.setRegistration(registrationField.getText().trim());
        vehicle.setDriver(driverComboBox.getValue());
        return vehicle;
    }

    private void updateSelectedVehicle() {
        selectedVehicle.setBrand(brandField.getText().trim());
        selectedVehicle.setModel(modelField.getText().trim());
        selectedVehicle.setRegistration(registrationField.getText().trim());
        selectedVehicle.setDriver(driverComboBox.getValue());
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        brandField.clear();
        modelField.clear();
        registrationField.clear();
        driverComboBox.getSelectionModel().clearSelection();
        selectedVehicle = null;
    }

    private void refreshTableView() {
        vehicleTableView.setItems(FXCollections.observableArrayList(vehicleDAO.getAllVehicles()));
    }
}