package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;


import java.util.HashMap;
import java.util.Map;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST = "jdbc:mysql://localhost:3306/myschema";
    private static final String LOGIN = "Alana";
    private static final String PASSWORD = "1234";


    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(HOST, LOGIN, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }


    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
            // Hibernate 5.4 SessionFactory example without XML
            Map<String, String> settings = new HashMap<>();
            settings.put("connection.driver_class", "com.mysql.jdbc.Driver");
            settings.put("dialect", "org.hibernate.dialect.MySQL8Dialect");
            settings.put("hibernate.connection.url", HOST);
            settings.put("hibernate.connection.username", LOGIN);
            settings.put("hibernate.connection.password", PASSWORD);
            settings.put("hibernate.current_session_context_class", "thread");
            settings.put("hibernate.show_sql", "true");
            settings.put("hibernate.format_sql", "true");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(settings).build();

            MetadataSources metadataSources = new MetadataSources(serviceRegistry);
            // metadataSources.addAnnotatedClass(Player.class);
            Metadata metadata = metadataSources.buildMetadata();

            // here we build the SessionFactory (Hibernate 5.4)
            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

            return sessionFactory;
        }
    }


