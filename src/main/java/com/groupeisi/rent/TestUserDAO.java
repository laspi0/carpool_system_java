package com.groupeisi.rent;

import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.entities.User;

import java.util.List;

public class TestUserDAO {

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // Charger tous les utilisateurs sauf l'administrateur
        List<User> drivers = userDAO.findDrivers();

        if (drivers.isEmpty()) {
            System.out.println("Aucun conducteur trouvé.");
        } else {
            System.out.println("Liste des conducteurs :");
            for (User driver : drivers) {
                System.out.println(driver.getFirstName() + " " + driver.getLastName() + " (" + driver.getEmail() + ")");
            }
        }
    }
}
