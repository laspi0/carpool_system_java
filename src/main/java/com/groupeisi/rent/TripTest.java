package com.groupeisi.rent;

import java.util.List;
import com.groupeisi.rent.DAO.TripDAO;
import com.groupeisi.rent.entities.Trip;

public class TripTest {

    public static void main(String[] args) {
        // Initialiser Hibernate
        TripDAO tripDAO = new TripDAO();

        // Appeler la méthode getAllTrips
        List<Trip> trips = tripDAO.getAllTrips();

        // Afficher les résultats
        if (trips != null) {
            for (Trip trip : trips) {
                System.out.println(trip.toString());
            }
        } else {
            System.out.println("Aucun voyage trouvé.");
        }
    }
}
