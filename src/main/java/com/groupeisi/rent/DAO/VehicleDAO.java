package com.groupeisi.rent.DAO;

import com.groupeisi.rent.Config.HibernateUtil;
import com.groupeisi.rent.entities.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;

import java.util.List;

public class VehicleDAO {
    private final SessionFactory sessionFactory;

    public VehicleDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(Vehicle vehicle) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(vehicle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save vehicle", e);
        }
    }

    public List<Vehicle> getAllVehicles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Vehicle", Vehicle.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Other methods...
}
