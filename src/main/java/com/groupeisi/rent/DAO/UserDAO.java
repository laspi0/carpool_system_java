package com.groupeisi.rent.DAO;

import com.groupeisi.rent.Config.HibernateUtil;
import com.groupeisi.rent.entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class UserDAO {

    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean deleteUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(user);
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
    public List<User> findAllExceptAdmin() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE role <> :role", User.class)
                    .setParameter("role", "admin")
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findDrivers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE role = :role", User.class)
                    .setParameter("role", "driver")
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Autres méthodes CRUD (update, delete, findById, etc.)
}
