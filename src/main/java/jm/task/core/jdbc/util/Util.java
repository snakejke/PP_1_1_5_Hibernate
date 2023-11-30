package jm.task.core.jdbc.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    private static final Logger logger = Logger.getLogger(Util.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/katadb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;


    public void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
            logger.log(Level.INFO,"Connection successful");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Connection failed...", ex);
        }
    }

    public Connection getConnection(){
        if(connection == null){
            connect();
        }
        return connection;
    }
}
