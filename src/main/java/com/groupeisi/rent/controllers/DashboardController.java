package com.groupeisi.rent.controllers;

import com.groupeisi.rent.Config.SessionManager;
import com.groupeisi.rent.entities.Reservation;
import com.groupeisi.rent.entities.User;
import com.groupeisi.rent.DAO.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private Label totalReservationsLabel;
    @FXML
    private Label mostFrequentDepartureCityLabel;
    @FXML
    private Label mostFrequentArrivalCityLabel;
    @FXML
    private PieChart reservationsByDepartureCity;
    @FXML
    private LineChart<String, Number> reservationsOverTime;
    @FXML
    private BarChart<String, Number> averagePriceByCity;

    private ReservationDAO reservationDao = new ReservationDAO();
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getLoggedInUser();
        if (currentUser != null) {
            List<Reservation> userReservations = reservationDao.getReservationsByUser(currentUser);

            updateTotalReservations(userReservations);
            updateMostFrequentCities(userReservations);
            updateReservationsByDepartureCity(userReservations);
            updateReservationsOverTime(userReservations);
            updateAveragePriceByCity(userReservations);
        } else {
            // Handle case when no user is logged in
            // For example, display a message or redirect to login page
        }
    }

    private void updateTotalReservations(List<Reservation> reservations) {
        totalReservationsLabel.setText(String.valueOf(reservations.size()));
    }

    private void updateMostFrequentCities(List<Reservation> reservations) {
        Map<String, Long> departureCityCounts = reservations.stream()
                .map(r -> r.getTrip().getDepartureCity())
                .collect(Collectors.groupingBy(city -> city, Collectors.counting()));

        Map<String, Long> arrivalCityCounts = reservations.stream()
                .map(r -> r.getTrip().getArrivalCity())
                .collect(Collectors.groupingBy(city -> city, Collectors.counting()));

        String mostFrequentDepartureCity = departureCityCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        String mostFrequentArrivalCity = arrivalCityCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        mostFrequentDepartureCityLabel.setText(mostFrequentDepartureCity);
        mostFrequentArrivalCityLabel.setText(mostFrequentArrivalCity);
    }

    private void updateReservationsByDepartureCity(List<Reservation> reservations) {
        Map<String, Long> departureCityCounts = reservations.stream()
                .collect(Collectors.groupingBy(r -> r.getTrip().getDepartureCity(), Collectors.counting()));

        reservationsByDepartureCity.getData().clear();
        for (Map.Entry<String, Long> entry : departureCityCounts.entrySet()) {
            reservationsByDepartureCity.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }

    private void updateReservationsOverTime(List<Reservation> reservations) {
        Map<LocalDate, Long> reservationCounts = reservations.stream()
                .collect(Collectors.groupingBy(r -> r.getTrip().getDepartureDate(), Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Réservations");

        LocalDate startDate = LocalDate.now().minusDays(6);
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            series.getData().add(new XYChart.Data<>(date.toString(), reservationCounts.getOrDefault(date, 0L)));
        }

        reservationsOverTime.getData().clear();
        reservationsOverTime.getData().add(series);
    }

    private void updateAveragePriceByCity(List<Reservation> reservations) {
        Map<String, Double> averagePrices = reservations.stream()
                .collect(Collectors.groupingBy(r -> r.getTrip().getArrivalCity(),
                        Collectors.averagingDouble(r -> r.getTrip().getPrice())));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Prix moyen");

        for (Map.Entry<String, Double> entry : averagePrices.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        averagePriceByCity.getData().clear();
        averagePriceByCity.getData().add(series);
    }
}