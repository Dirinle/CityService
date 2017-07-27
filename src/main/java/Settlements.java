import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yaroslav on 03.07.17.
 */
public class Settlements {
    public static void main(String[] args) {

    }
}


class Settlement {
    private String country;
    private String area;
    String cityName;
    Integer population = null;
    ArrayList<Structure> structures = null;

    private void getInformationFromInternet(String city) {

        String[] cityName = city.split(" ");
        try {
            URL url = new URL("http://www.worldatlas.com/citypops.htm");
            Scanner scanner = new Scanner(url.openStream());
            boolean fName = findName(scanner, cityName);
            if (!fName) {

                return;
            }
            findAreaAndCountry(scanner);
            findPopulation(scanner);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void findAreaAndCountry(Scanner scanner) {
        Pattern pattern = Pattern.compile("countrys/[a-z]*.");//pattern for finding area
        Matcher matcher;
        while (scanner.hasNext()) {//find country and area
            String string = scanner.next();//string with inform. about area AND country
            matcher = pattern.matcher(string);
            if (matcher.find()) {
                String tmp = matcher.group();
                area = tmp.substring(9, tmp.length() - 1);//cut from result ""
                pattern = Pattern.compile("htm\">[a-z]*.*");//pattern for finding country
                matcher = pattern.matcher(string);
                if (matcher.find())
                    country = matcher.group().substring(5);
                {
                    if (country.endsWith("</a>")) {//if country contains only 1 word
                        country = country.substring(0, country.length() - 4);//cut load part
                    } else {
                        do {
                            country += " " + scanner.next();
                        }
                        while (!scanner.next().endsWith(">"));//read all words until end of tag
                        country = country.substring(0, country.length() - 4);//cut load part
                    }
                }
                return;
            }
        }
    }

    private boolean findName(Scanner scanner, String[] cityName) {
        while (scanner.hasNext()) {
            if (scanner.next().contains(cityName[0])) {//checking first word of city
                int i = 0;
                for (; (i + 1 < cityName.length) && (scanner.next().contains(cityName[i + 1])); i++) ;
                if (i + 1 == cityName.length) {
                    return true;
                }
            }
        }
        return false;
    }

    private void findPopulation(Scanner scanner) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher;
        String string;
        while (scanner.hasNext()) {
            string = scanner.next();
            matcher = pattern.matcher(string);
            if (matcher.find()) {
                population = Integer.parseInt(matcher.group());
                return;
            }
        }
    }

    public Settlement(String cityName, String country, Integer population, ArrayList<Structure> structures) {
        this.cityName = cityName;
        this.country = country;
        this.population = population;
        this.structures = structures;
    }

    public Settlement(String cityName, ArrayList<Structure> structures) {
        getInformationFromInternet(cityName);
        this.cityName = cityName;
        this.structures = structures;
    }

    public Settlement() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ");
    }

    public void info() {
        System.out.println("area = " + area);
        System.out.println("country = " + country);
        System.out.println("population = " + population);
    }

    double getEmployment() {
        return (double) structures.stream().mapToInt(x -> x.getStaff()).sum() / (double) population * 100;
    }

    int getIncome() {
        return structures.stream().mapToInt(x -> x.calculateIncome()).sum();
    }

    public void addToDatabase(Connection connection) {
        String statementInsert = "INSERT INTO myProjectDB"
                + "(cityName, population, country, income, employment) values ("
                + "\'" + cityName + "\', " + population + ", \'" + country + "\', " + getIncome() + ", " + getEmployment() + ");";
        String statementSelect = "Select cityName from myProjectDB;";
        List<String> cities = new ArrayList<>();
        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(statementSelect);
            while (resultSet.next()) {
                cities.add(resultSet.getString("cityName"));
            }
            if (cities.add(cityName))
                statement.execute(statementInsert);
        } catch (SQLException e) {
            System.out.println("Statement was not executed");
            e.printStackTrace();
        }
    }
}