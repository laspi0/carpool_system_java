package com.groupeisi.rent.DAO;

import com.groupeisi.rent.Config.HibernateUtil;
import com.groupeisi.rent.entities.Reservation;
import com.groupeisi.rent.entities.Trip;
import com.groupeisi.rent.entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ReservationDAO {

    public void save(Reservation reservation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(reservation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Reservation> getReservationsByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Reservation> query = session.createQuery(
                    "SELECT r FROM Reservation r JOIN FETCH r.trip t JOIN FETCH t.vehicle WHERE r.passenger = :user",
                    Reservation.class
            );
            query.setParameter("user", user);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Reservation> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Reservation", Reservation.class).list();
        }
    }

    public boolean deleteReservation(Reservation reservation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(reservation);
            // Mettre à jour le nombre de sièges disponibles dans le voyage
            Trip trip = reservation.getTrip();
            trip.setAvailableSeats(trip.getAvailableSeats() + 1);
            session.update(trip);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Reservation reservation = session.get(Reservation.class, id);
            if (reservation != null) {
                session.delete(reservation);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Reservation findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Reservation.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(Reservation reservation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(reservation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}