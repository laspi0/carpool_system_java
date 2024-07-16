package com.groupeisi.rent.controllers.vehicle;

import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.DAO.VehicleDAO;
import com.groupeisi.rent.entities.User;
import com.groupeisi.rent.entities.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableView();
        loadDrivers();
        loadVehicles(); // Charge les véhicules au démarrage
    }

    private void initializeTableView() {
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        registrationColumn.setCellValueFactory(new PropertyValueFactory<>("registration"));
        driverColumn.setCellValueFactory(new PropertyValueFactory<>("driver"));
    }

    private void loadDrivers() {
        List<User> drivers = userDAO.findDrivers();
        driverComboBox.setItems(FXCollections.observableArrayList(drivers));
        driverComboBox.setCellFactory((comboBox) -> {
            return new javafx.scene.control.ListCell<User>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getFirstName() + " " + item.getLastName());
                    }
                }
            };
        });
        driverComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user == null) {
                    return null;
                } else {
                    return user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")";
                }
            }

            @Override
            public User fromString(String string) {
                return null; // Not used
            }
        });
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        vehicleTableView.setItems(FXCollections.observableArrayList(vehicles));
    }

    @FXML
    private void addVehicle() {
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String registration = registrationField.getText().trim();
        User selectedDriver = driverComboBox.getValue();

        if (brand.isEmpty() || model.isEmpty() || registration.isEmpty() || selectedDriver == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Vehicle newVehicle = new Vehicle();
        newVehicle.setBrand(brand);
        newVehicle.setModel(model);
        newVehicle.setRegistration(registration);
        newVehicle.setDriver(selectedDriver);

        vehicleDAO.save(newVehicle);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Le véhicule a été ajouté avec succès.");
        clearFields();
        loadVehicles(); // Recharge les véhicules après ajout
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        brandField.clear();
        modelField.clear();
        registrationField.clear();
        driverComboBox.getSelectionModel().clearSelection();
    }
}
