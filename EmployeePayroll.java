import java.util.ArrayList;
import java.util.Scanner;

// ─── Abstract Employee ───
abstract class Employee {
    protected String name;
    protected int empId;
    protected String department;

    public Employee(String name, int empId, String department) {
        this.name = name; this.empId = empId; this.department = department;
    }

    public abstract double calculateGrossSalary();

    public double calculateTax(double gross) {
        if (gross <= 20000) return 0;
        if (gross <= 50000) return gross * 0.10;
        return gross * 0.20;
    }

    public void generatePayStub() {
        double gross      = calculateGrossSalary();
        double tax        = calculateTax(gross);
        double deductions = gross * 0.05; // PF
        double net        = gross - tax - deductions;

        System.out.println("===================== PAY STUB =====================");
        System.out.printf("Employee  : %-20s | ID: %d%n", name, empId);
        System.out.printf("Department: %s%n", department);
        System.out.printf("Gross Salary : Rs. %8.2f%n", gross);
        System.out.printf("Tax          : Rs. %8.2f%n", tax);
        System.out.printf("PF Deduction : Rs. %8.2f%n", deductions);
        System.out.printf("NET SALARY   : Rs. %8.2f%n", net);
        System.out.println("====================================================");
    }

    public int getEmpId() { return empId; }
    public String getName() { return name; }

    public void setDepartment(String dept) { this.department = dept; }
}

// ─── Full Time Employee ───
class FullTimeEmployee extends Employee {
    private double basicSalary;
    private double hra, da;

    public FullTimeEmployee(String name, int id, String dept, double basic) {
        super(name, id, dept);
        this.basicSalary = basic;
        this.hra  = basic * 0.20; // 20% HRA
        this.da   = basic * 0.10; // 10% DA
    }

    @Override
    public double calculateGrossSalary() { return basicSalary + hra + da; }

    public void setSalary(double basic) {
        basicSalary = basic; hra = basic * 0.20; da = basic * 0.10;
    }
}

// ─── Part Time Employee ───
class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String name, int id, String dept, double rate, int hours) {
        super(name, id, dept);
        this.hourlyRate  = rate;
        this.hoursWorked = hours;
    }

    @Override
    public double calculateGrossSalary() { return hourlyRate * hoursWorked; }
}

// ─── Main ───
public class EmployeePayroll {
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void addEmployee() {
        System.out.println("1. Full-Time  2. Part-Time");
        int type = sc.nextInt(); sc.nextLine();
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("ID: ");   int id = sc.nextInt(); sc.nextLine();
        System.out.print("Dept: "); String dept = sc.nextLine();

        if (type == 1) {
            System.out.print("Basic Salary: ");
            employees.add(new FullTimeEmployee(name, id, dept, sc.nextDouble()));
        } else {
            System.out.print("Hourly Rate: ");  double rate = sc.nextDouble();
            System.out.print("Hours Worked: "); int hrs = sc.nextInt();
            employees.add(new PartTimeEmployee(name, id, dept, rate, hrs));
        }
        System.out.println("Employee added!");
    }

    public static void generateAllPayStubs() {
        if (employees.isEmpty()) { System.out.println("No employees."); return; }
        for (Employee e : employees) e.generatePayStub();
    }

    public static void updateSalary() {
        System.out.print("Enter Employee ID: "); int id = sc.nextInt();
        for (Employee e : employees) {
            if (e.getEmpId() == id && e instanceof FullTimeEmployee) {
                System.out.print("New basic salary: ");
                ((FullTimeEmployee) e).setSalary(sc.nextDouble());
                System.out.println("Updated!"); return;
            }
        }
        System.out.println("Full-time employee not found.");
    }

    public static void main(String[] args) {
        System.out.println("===== Employee Payroll System =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Add Employee  2. Generate Pay Stubs  3. Update Salary  4. Exit");
            System.out.print("Choice: ");
            switch (sc.nextInt()) {
                case 1: addEmployee();        break;
                case 2: generateAllPayStubs(); break;
                case 3: updateSalary();        break;
                case 4: running = false;       break;
                default: System.out.println("Invalid.");
            }
        }
        sc.close();
    }
}
