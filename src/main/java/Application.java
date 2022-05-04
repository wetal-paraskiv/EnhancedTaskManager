import java.util.ArrayList;

public class Application {

    public static void main(String[] args) {

        CRUD crud = new CRUD();

        CreateTables createTables = new CreateTables();
        createTables.createAllTables();

        switch (args[0]) {
            case "-createUser": {
                crud.createUser(args[1].substring(4), args[2].substring(4), args[3].substring(4));
                break;
            }
            case "-showAllUsers": {
                crud.showAllUsers();
                break;
            }
            case "-addTask": {
                ArrayList<String> users = new ArrayList<>();
                for (int i = 1; i < args.length - 2; i++) {
                    users.add(args[i].substring(4));
                }
                String tt = args[args.length - 2].substring(4);
                String td = args[args.length - 1].substring(4);

                crud.addTask(users, tt, td);
                break;
            }
            case "-showTasks": {
                crud.showTasks(args[1].substring(4));
                break;
            }
            default: {
                System.out.println("Unknown method name!");
                break;
            }
        }
    }
}
