package epam.webtech.utils;

import epam.webtech.exceptions.DatabaseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcService {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private static boolean isInitialized = false;

   // private final Logger logger = LogManager.getLogger();

    private void init() throws DatabaseException {
        try (InputStream input = new FileInputStream("src/main/resources/conf/conf.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            dbUrl = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.user");
            dbPassword = prop.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            isInitialized = true;
        } catch (ClassNotFoundException e) {
    //        logger.error(e);
            throw new DatabaseException("Failed to initialize JdbcService. MySQL JDBC Driver is unavailable.");
        } catch (IOException e) {
     //       logger.error(e);
            throw new DatabaseException("Failed to initialize JdbcService. Configuration file is unavailable.");
        }
    }

    private JdbcService() {
    }

    public Connection getConnection() throws DatabaseException {
        if (!isInitialized) {
            JdbcService.SingletonHandler.INSTANCE.init();
        }
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
        //    logger.error(e);
            throw new DatabaseException("Failed to establish database connection.");
        }
    }

    public static JdbcService getInstance() {
        return JdbcService.SingletonHandler.INSTANCE;
    }

    private static class SingletonHandler {
        static final JdbcService INSTANCE = new JdbcService();
    }
}
