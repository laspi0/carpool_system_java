package com.groupeisi.rent.DAO;

import com.groupeisi.rent.Config.HibernateUtil;
import com.groupeisi.rent.entities.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

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
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Vehicle", Vehicle.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Vehicle vehicle) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(vehicle);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Impossible de supprimer le véhicule car il est référencé ailleurs", e);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Échec de la suppression du véhicule", e);
        }
    }

    public void update(Vehicle vehicle) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(vehicle);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update vehicle", ex);
        }
    }
}
