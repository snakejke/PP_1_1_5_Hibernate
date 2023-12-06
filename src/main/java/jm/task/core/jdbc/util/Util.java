package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import java.util.Properties;
import java.util.logging.Logger;



public class Util {

    private static final Logger logger = Logger.getLogger(Util.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/katadb";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DIALECT = "org.hibernate.dialect.MariaDBDialect";//org.hibernate.dialect.MySQLDialect
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private SessionFactory sessionFactory;
    private Connection connection;


    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();

                settings.put(Environment.DRIVER, DRIVER);
                settings.put(Environment.URL, URL);
                settings.put(Environment.USER, USER);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, DIALECT);
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "none");

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                logger.info("Session factory created successfully.");
            } catch (Exception e) {
                logger.severe("Problem creating session factory: " + e.getMessage());
                throw new RuntimeException("Problem creating session factory", e);
            }
        }
        return sessionFactory;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.log(Level.INFO, "Connection successful");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Connection failed...", ex);
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            connect();
        }
        return connection;
    }


}
