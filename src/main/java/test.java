
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
/**
 * Created by yaroslav on 22.04.17.
 */
public class test {
    public static void main(String[] args) {
        Structure []  structures1 = new Structure[]{new Schools(1, 1, 1, 1),
                new Hospitals(1, 1, 1, 1),
                new Shops(1, 1, 1, 1),
                new Factories(1, 1, 1)};
        ArrayList <Structure>  structures= new ArrayList<>(Arrays.asList(structures1));
        Map<String, Settlement> settlements = new HashMap<>();
        Map<String, Tree> trees = new HashMap<>();
        String input ="";
        System.out.println("For output commands enter \"help\"");
        Scanner scanner = new Scanner(System.in);
        Connection connection = getConnection();
        settlements.put("Paris",new Settlement("Paris", structures));
        settlements.put("Paris",new Settlement("Paris", structures));
        while (!input.equals("exit")) {
            System.out.println("-----------------enter command------------------");
            input = scanner.next();
            switch (input) {
                case "cleandb":{
                    try {
                        Statement statement = connection.createStatement();
                        statement.execute("TRUNCATE TABLE myProjectDB");
                    } catch (SQLException e) {
                        System.out.println("Cleaning was not executed");
                        e.printStackTrace();
                    }
                    break;
                }
                case "updatedb":{
                    settlements.forEach((x,y)->y.addToDatabase(connection));
                    break;
                }
                case "help": {
                    help();
                    break;
                }
                case "createtree": {
                    System.out.println("Enter name of tree");
                    String name = scanner.next();
                    trees.put(name, Tree.ConsoleInputeTree());
                    break;
                }
                case "outputtree": {
                    System.out.println("Enter name of tree");
                    String name = scanner.next();
                    if (trees.get(name) == null) {
                        System.out.println("Tree with this name does not exist");
                        break;
                    }
                    trees.get(name).outputWidht();
                    break;
                }
                case "updatetree": {
                    System.out.println("Enter name of tree");
                    String name = scanner.next();
                    if (trees.get(name) == null) {
                        System.out.println("Tree with this name does not exist");
                        break;
                    }
                    trees.get(name).update();
                    break;
                }
                case "createcity": {
                    System.out.println("Enter name of city");
                    String cityName = scanner.next();
                    System.out.println("If you want create existing and big city (more 3  millions) enter \"exist\"");
                    System.out.println("else \"noexist\"");
                    input = scanner.next();
                    System.out.println("input = " + input);
                    if (input.equals("exist")) {
                        settlements.put(cityName, new Settlement(cityName, getStructuresFromConsole()));
                    } else if (input.equals("noexist")) {
                        System.out.println("Enter population of city");
                        Integer population = scanner.nextInt();
                        System.out.println("Enter country of city");
                        String country = scanner.next();
                        settlements.put(cityName, new Settlement(cityName, country, population, getStructuresFromConsole()));
                    } else continue;
                    break;
                }
                case "info": {
                    System.out.println("Enter name of city");
                    String cityName = scanner.next();
                    settlements.get(cityName).info();
                    System.out.println("Employment of " + cityName + " is " + settlements.get(cityName).getEmployment() + "%");
                    System.out.println("Income of " + cityName + " is " + settlements.get(cityName).getIncome());
                    break;
                }
                case "exit": {
                    System.out.println("Program has completed execution");
                    return;
                }
                default:
                    System.out.println("Command is not exist");

            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection was not close");
            e.printStackTrace();
        }
    }

    static Connection getConnection() {
        String server = "jdbc:mysql://localhost:3306/test_db";
        String password = "Water1815";//System.console().readLine();
        Connection result = null;
        try {
            result = DriverManager.getConnection(server, "Dirinle", password);
        } catch (SQLException e) {
            System.out.println("Not connected!");
            e.printStackTrace();
        }
        return result;
    }

    static void help() {
        System.out.println("Commands:");
        System.out.println("\tcreatecity - creating new settlement");
        System.out.println("\tupdatedb- updating data base of cities");
        System.out.println("\tcleandb - cleaning data base");
        System.out.println("\tinfo - full information about city");
        System.out.println("\tcreatetree - creating new tree");
        System.out.println("\tupdatetree - updating tree");
        System.out.println("\toutputtree - output tree in width");
        System.out.println("\texit - exit of program");
    }

    static ArrayList<Structure> getStructuresFromConsole() {
        ArrayList<Structure> result = new ArrayList<>();
        System.out.println("Enter information about schools");
        result.add(Schools.getSchoolsFromConsole());
        System.out.println("Enter information about hospitals");
        result.add(Hospitals.getHospitalsFromConsole());
        System.out.println("Enter information about shops");
        result.add(Shops.getShopsFromConsole());
        System.out.println("Enter information about factories");
        result.add(Factories.getFactoriesFromConsole());
        return result;
    }
}
