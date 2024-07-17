package com.groupeisi.rent.controllers.trip;

import com.groupeisi.rent.Config.SessionManager;
import com.groupeisi.rent.DAO.ReservationDAO;
import com.groupeisi.rent.DAO.TripDAO;
import com.groupeisi.rent.entities.Reservation;
import com.groupeisi.rent.entities.Trip;
import com.groupeisi.rent.entities.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;

public class MyTripController {
    @FXML
    private TableView<Trip> tripTable;
    @FXML
    private TableColumn<Trip, String> departureCityColumn;
    @FXML
    private TableColumn<Trip, String> arrivalCityColumn;
    @FXML
    private TableColumn<Trip, LocalDate> departureDateColumn;
    @FXML
    private TableColumn<Trip, Integer> availableSeatsColumn;
    @FXML
    private TableColumn<Trip, Double> priceColumn;
    @FXML
    private TableColumn<Trip, Void> actionColumn;

    private TripDAO tripDAO = new TripDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();

    @FXML
    public void initialize() {
        System.out.println("MyTripController initialized");
        setupTableColumns();
        refreshTripTable();
        addButtonToTable();
    }

    public void setLoggedInUser(User user) {
        System.out.println("Logged in user set in MyTripController: " + (user != null ? user.getEmail() : "null"));
    }

    private void setupTableColumns() {
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        arrivalCityColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalCity"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void refreshTripTable() {
        tripTable.setItems(FXCollections.observableArrayList(tripDAO.getAllTrips()));
    }

    private void addButtonToTable() {
        Callback<TableColumn<Trip, Void>, TableCell<Trip, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Trip, Void> call(final TableColumn<Trip, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Réserver");
                    {
                        btn.setOnAction((event) -> {
                            Trip trip = getTableView().getItems().get(getIndex());
                            handleReservation(trip);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void handleReservation(Trip trip) {
        System.out.println("Handling reservation. Logged in user: " + (SessionManager.getLoggedInUser() != null ? SessionManager.getLoggedInUser().getEmail() : "null"));
        if (SessionManager.getLoggedInUser() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez vous connecter pour faire une réservation.");
            return;
        }

        if (trip.getAvailableSeats() > 0) {
            try {
                Reservation reservation = new Reservation();
                reservation.setDateReservation(LocalDate.now());
                reservation.setTrip(trip);
                reservation.setPassenger(SessionManager.getLoggedInUser());

                reservationDAO.save(reservation);

                trip.setAvailableSeats(trip.getAvailableSeats() - 1);
                tripDAO.update(trip);

                refreshTripTable();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation réussie pour : " + trip);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la réservation : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Attention", "Pas de places disponibles pour : " + trip);
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