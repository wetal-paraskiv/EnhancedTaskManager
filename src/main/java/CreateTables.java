import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateTables {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    private static final String url = resourceBundle.getString("db.url");
    private static final String owner = resourceBundle.getString("db.username");
    private static final String password = resourceBundle.getString("db.password");

    public void createAllTables() {
        createTableUsers();
        createTableTasks();
    }

    private void createTableUsers() {
        Connection connection;
        Statement statement;
        String query = "create table if not exists users(" +
                       "user_id int primary key auto_increment," +
                       "first_name varchar(50) not null," +
                       "last_name varchar(50) not null," +
                       "user_name varchar(50) unique not null)";
        try {
            connection = DriverManager.getConnection(url, owner, password);
            statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(CreateTables.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void createTableTasks() {
        Connection connection;
        Statement statement;
        String query = "create table if not exists tasks(" +
                       "task_id int primary key auto_increment," +
                       "user_name varchar(50) not null," +
                       "title varchar(50) not null," +
                       "description varchar(255) not null)";
        try {
            connection = DriverManager.getConnection(url, owner, password);
            statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(CreateTables.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
