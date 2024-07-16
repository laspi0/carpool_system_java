package com.groupeisi.rent.DAO;

import com.groupeisi.rent.Config.HibernateUtil;
import com.groupeisi.rent.entities.Trip;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TripDAO {

    public void save(Trip trip) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(trip);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Trip trip) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(trip);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Trip trip) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(trip);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Trip getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Trip.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Trip> getAllTrips() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Trip", Trip.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}