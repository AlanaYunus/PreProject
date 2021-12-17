package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.hql.internal.ast.util.SessionFactoryHelper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.logging.Logger;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
                "age TINYINT NOT NULL)";

        session.createSQLQuery(sql).addEntity(User.class);

        transaction.commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String sql = "DROP TABLE IF EXISTS users";

        session.createSQLQuery(sql).addEntity(User.class);

        transaction.commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                session.beginTransaction();
                session.delete(user);
                session.getTransaction().commit();
            } else {
                System.out.println("User doesn't exist with provideded Id..");
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<User> getAllUsers() {

        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        List<User> users = null;

        try {
            transaction = session.beginTransaction();
            users = (List<User>) session.createCriteria(User.class).list();
            transaction.commit();

        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            for (User user : getAllUsers()) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();

            if (transaction != null) transaction.rollback();
        } finally {
            try {
                if (session != null) session.close();
            } catch (Exception ex) {
            }
        }
    }
}
