import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Scanner;

/**
 * Created by yaroslav on 03.07.17.
 */
public class StructuresAl {
    public static void main(String[] args) {

    }
}

class CustomerSellerStructure extends Structure {
    Integer averagePayment;
    Integer averageSalary;

    public CustomerSellerStructure(StructureType type, Integer users, Integer staff, Integer averagePayment, Integer averageSalary) {
        super(type, users, staff);
        this.averagePayment = averagePayment;
        this.averageSalary = averageSalary;
    }

    @Override
    Integer calculateIncome() {
        return averagePayment * users - averageSalary * staff;
    }
}

class Schools extends CustomerSellerStructure {

    public Schools(Integer users, Integer staff, Integer averagePayment, Integer averageSalary) {
        super(StructureType.EDUCATIONAL, users, staff, averagePayment, averageSalary);
        description = "Schools education structure";
    }

    public static Schools getSchoolsFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of users");
        int users = scanner.nextInt();
        System.out.println("Enter number of staff");
        int staff = scanner.nextInt();
        System.out.println("Enter average payment");
        int averagePayment = scanner.nextInt();
        System.out.println("Enter average Salary");
        int averageSalary = scanner.nextInt();
        return new Schools(users, staff, averagePayment, averageSalary);
    }
}

class Hospitals extends CustomerSellerStructure {

    public Hospitals(Integer users, Integer staff, Integer averagePayment, Integer averageSalary) {
        super(StructureType.HEALTHY, users, staff, averagePayment, averageSalary);
        description = "Hospitals health structure";
    }
    public static Hospitals getHospitalsFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of users");
        int users = scanner.nextInt();
        System.out.println("Enter number of staff");
        int staff = scanner.nextInt();
        System.out.println("Enter average payment");
        int averagePayment = scanner.nextInt();
        System.out.println("Enter average Salary");
        int averageSalary = scanner.nextInt();
        return new Hospitals(users, staff, averagePayment, averageSalary);
    }
}

class Shops extends CustomerSellerStructure {

    public Shops(Integer users, Integer staff, Integer averagePayment, Integer averageSalary) {
        super(StructureType.COMMERCIAL, users, staff, averagePayment, averageSalary);
        description = "Shops commercial structure";
    }

    public static Shops getShopsFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of users");
        int users = scanner.nextInt();
        System.out.println("Enter number of staff");
        int staff = scanner.nextInt();
        System.out.println("Enter average payment");
        int averagePayment = scanner.nextInt();
        System.out.println("Enter average Salary");
        int averageSalary = scanner.nextInt();
        return new Shops(users, staff, averagePayment, averageSalary);
    }
}

class Factories extends Structure {
    enum FactoriesType {
        LIGHTINDUSTRY, HEAVYINDUSTRY, ARGICULTURE
    }

    FactoriesType factoriesType;

    public Factories(Integer staff, Integer costs, Integer income, FactoriesType fType) {
        super(StructureType.INDUSTRY, staff, costs, income);
        factoriesType = fType;
        description = "Factories " + factoriesType.toString().toLowerCase() + "type";
    }

    public Factories(Integer staff, Integer costs, Integer income) {
        super(StructureType.INDUSTRY, staff, costs, income);
        factoriesType = null;
        description = "Factoryes unspecified type";
    }

    @Override
    Integer calculateIncome() {
        return getIncome() - getCosts();
    }


    public static Factories getFactoriesFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of staff");
        int staff = scanner.nextInt();
        System.out.println("Enter costs ");
        int costs = scanner.nextInt();
        System.out.println("Enter income ");
        int income = scanner.nextInt();
        return new Factories( staff, costs, income);
    }
}

class UnsprecifiedStructure extends Structure {

    public UnsprecifiedStructure(StructureType type, Integer staff, Integer costs, Integer income) {
        super(type, staff, costs, income);
    }

    @Override
    Integer calculateIncome() {
        return getIncome() - getCosts();
    }
}

abstract class Structure {// any social structure; for example school or hospital

    protected enum StructureType {
        UNSPECIFIED, EDUCATIONAL, COMMERCIAL, HEALTHY, INDUSTRY
    }

    protected StructureType type;
    protected Integer users;
    protected Integer staff;
    String description;

    private Integer costs;
    private Integer income;

    abstract Integer calculateIncome();

    public Structure(StructureType type, Integer staff, Integer costs, Integer income) {
        this.type = type;
        this.staff = staff;
        this.costs = costs;
        this.income = income;
    }

    public Structure(StructureType type, Integer users, Integer staff) {
        this.type = type;
        this.users = users;
        this.staff = staff;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public void setStaff(Integer staff) {
        this.staff = staff;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCosts(Integer costs) {
        this.costs = costs;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Integer getCosts() {
        return costs;
    }

    public Integer getIncome() {
        return income;
    }

    public Integer getStaff() {
        return staff;
    }

}
