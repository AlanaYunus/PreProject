package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;
import org.hibernate.hql.internal.ast.util.SessionFactoryHelper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.logging.Logger;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory sessionFactory = getSessionFactory();

    private UserDaoHibernateImpl() {
    }

    private static UserDaoHibernateImpl instance;

    public static UserDaoHibernateImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoHibernateImpl();
        }
        return instance;
    }

    @Override
    public void createUsersTable() {
        Session session = null;
        Transaction transaction = null;

        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
                "age TINYINT NOT NULL)";

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            session.createSQLQuery(sql).addEntity(User.class);

            transaction.commit();

        } catch (HibernateException e) {
            e.printStackTrace();

        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = null;
        Transaction transaction = null;

        String sql = "DROP TABLE IF EXISTS users";

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            session.createSQLQuery(sql).addEntity(User.class);

            transaction.commit();

        } catch (HibernateException e) {
            e.printStackTrace();

        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception ex) {
            }
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();

        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception ex) {
            }
        }
    }


    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        Session session = null;

        try {
            session = sessionFactory.getCurrentSession();
            User user = session.get(User.class, id);

            if (user != null) {
                tx = session.beginTransaction();
                session.delete(user);
                tx.commit();
            } else {
                System.out.println("User doesn't exist with provideded Id..");
            }
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception ex) {
            }
        }
    }


    @Override
    public List<User> getAllUsers() {

        Session session = null;
        Transaction transaction = null;
        List<User> users = null;

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();
            users = (List<User>) session.createCriteria(User.class).list();
            transaction.commit();

        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception ex) {
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = null;
        Transaction transaction = null;

        String sql = "TRUNCATE TABLE users";

        try {
            session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            session.createSQLQuery(sql).executeUpdate();

            transaction.commit();

        } catch (Exception ex) {
            ex.printStackTrace();

            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (Exception ex) {
            }
        }
    }
}
