import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CRUD {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    private static final String url = resourceBundle.getString("db.url");
    private static final String owner = resourceBundle.getString("db.username");
    private static final String password = resourceBundle.getString("db.password");


    public void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnsNumber = resultSetMetaData.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(resultSetMetaData.getColumnName(i) + ": " + columnValue);
            }
            System.out.println();
        }
    }

    public void showAllUsers() {
        String query = "select first_name, last_name, Count(task_id) \n" +
                       "from users\n" +
                       "join tasks \n" +
                       "on users.user_name = tasks.user_name\n" +
                       "group by first_name";
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        try {
            connection = DriverManager.getConnection(url, owner, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            System.out.println("\n\tList of Users:");
            printResultSet(resultSet);
            System.out.println();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(CRUD.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void createUser(String fn, String ln, String un) {
        if (!exists(un)) {
            String query = "INSERT INTO users (first_name, last_name, user_name) VALUES (?, ?, ?);";

            try (Connection connection = DriverManager.getConnection(url, owner, password);
                 PreparedStatement pst = connection.prepareStatement(query)) {

                pst.setString(1, fn);
                pst.setString(2, ln);
                pst.setString(3, un);
                pst.executeUpdate();

                System.out.println("Successfully saved user to db..\n");

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(CRUD.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            System.out.println("Choose another unique UserName (this UserName is already taken!)\n");
        }
    }

    private boolean exists(String un) {
        String query = String.format("select user_name from users where user_name = '%1$s'", un);
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        try {
            connection = DriverManager.getConnection(url, owner, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return true;

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(CRUD.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    public void addTask(ArrayList<String> users, String tt, String td) {
        for (String un : users) {
            if (exists(un)) {
                String query = "INSERT INTO tasks (user_name, title, description) VALUES (?, ?, ?);";

                try (Connection connection = DriverManager.getConnection(url, owner, password);
                     PreparedStatement pst = connection.prepareStatement(query)) {

                    pst.setString(1, un);
                    pst.setString(2, tt);
                    pst.setString(3, td);
                    pst.executeUpdate();

                    System.out.println("Successfully saved Task to db..\n");

                } catch (SQLException ex) {
                    Logger lgr = Logger.getLogger(CRUD.class.getName());
                    lgr.log(Level.SEVERE, ex.getMessage(), ex);
                }
            } else {
                System.out.println("Choose existing UserName (ERROR 404! Not found)\n");
            }
        }
    }

    public void showTasks(String un) {
        if (exists(un)) {
            String query = String.format("select title, description from tasks where user_name = '%1$s';", un);
            Connection connection;
            Statement statement;
            ResultSet resultSet;
            try {
                connection = DriverManager.getConnection(url, owner, password);
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);

                System.out.println("\n\t" + un + " tasks:");
                printResultSet(resultSet);
                System.out.println();

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(CRUD.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            System.out.println("Choose existing UserName (ERROR 404!)\n");
        }
    }
}
