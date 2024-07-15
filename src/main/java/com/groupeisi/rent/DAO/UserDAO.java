package com.groupeisi.rent.DAO;

import com.groupeisi.rent.Config.HibernateUtil;
import com.groupeisi.rent.entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    // Autres méthodes CRUD
}