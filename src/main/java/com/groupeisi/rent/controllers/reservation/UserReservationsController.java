package com.groupeisi.rent.controllers.reservation;

import com.groupeisi.rent.Config.SessionManager;
import com.groupeisi.rent.DAO.ReservationDAO;
import com.groupeisi.rent.entities.Reservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
public class UserReservationsController {

    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, String> departureCityColumn;
    @FXML
    private TableColumn<Reservation, String> arrivalCityColumn;
    @FXML
    private TableColumn<Reservation, String> vehicleModelColumn;
    @FXML
    private TableColumn<Reservation, String> departureDateColumn;
    @FXML
    private TableColumn<Reservation, String> priceColumn;
    @FXML
    private TableColumn<Reservation, Void> actionColumn;

    private ReservationDAO reservationDAO = new ReservationDAO();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadReservations();
    }

    private void setupTableColumns() {
        departureCityColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrip().getDepartureCity()));

        arrivalCityColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrip().getArrivalCity()));

        vehicleModelColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrip().getVehicle().getModel()));

        departureDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTrip().getDepartureDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        priceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f", cellData.getValue().getTrip().getPrice())));

        // Configuration de la colonne d'action (suppression)
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Annuler");

            {
                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handleDeleteReservation(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationDAO.getReservationsByUser(SessionManager.getLoggedInUser());
        if (reservations != null) {
            reservationTable.setItems(FXCollections.observableArrayList(reservations));
        } else {
            System.out.println("No reservations found for the user");
            reservationTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void handleDeleteReservation(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la réservation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = reservationDAO.deleteReservation(reservation);
            if (deleted) {
                loadReservations();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "La réservation a été supprimée avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression de la réservation.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}