import java.util.ArrayList;
import java.util.Scanner;

// ───── Student class (Encapsulation) ─────
class Student {
    private String name;
    private int rollNo;
    private double marks;

    public Student(String name, int rollNo, double marks) {
        this.name   = name;
        this.rollNo = rollNo;
        this.marks  = marks;
    }

    public String getName()   { return name; }
    public int getRollNo()    { return rollNo; }
    public double getMarks()  { return marks; }

    public void setName(String name)     { this.name  = name; }
    public void setMarks(double marks)   { this.marks = marks; }

    public String getGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return String.format("Roll: %3d | Name: %-20s | Marks: %5.1f | Grade: %s",
                rollNo, name, marks, getGrade());
    }
}

// ───── Student Management System ─────
public class StudentManagementSystem {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void addStudent() {
        System.out.print("Enter name: ");
        String name = sc.next(); sc.nextLine();
        System.out.print("Enter roll number: ");
        int roll = sc.nextInt();
        System.out.print("Enter marks (out of 100): ");
        double marks = sc.nextDouble();
        students.add(new Student(name, roll, marks));
        System.out.println("Student added successfully!");
    }

    public static void displayAll() {
        if (students.isEmpty()) { System.out.println("No students found."); return; }
        System.out.println("\n========== Student Records ==========");
        for (Student s : students) System.out.println(s);
    }

    public static void deleteStudent() {
        System.out.print("Enter roll number to delete: ");
        int roll = sc.nextInt();
        students.removeIf(s -> s.getRollNo() == roll);
        System.out.println("Student deleted (if found).");
    }

    public static void updateStudent() {
        System.out.print("Enter roll number to update: ");
        int roll = sc.nextInt();
        for (Student s : students) {
            if (s.getRollNo() == roll) {
                System.out.print("New name: "); String n = sc.next(); s.setName(n);
                System.out.print("New marks: ");  s.setMarks(sc.nextDouble());
                System.out.println("Updated successfully!"); return;
            }
        }
        System.out.println("Student not found.");
    }

    public static void main(String[] args) {
        System.out.println("===== Student Management System =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Add Student");
            System.out.println("2. Display All");
            System.out.println("3. Delete Student");
            System.out.println("4. Update Student");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int ch = sc.nextInt();
            switch (ch) {
                case 1: addStudent();     break;
                case 2: displayAll();     break;
                case 3: deleteStudent();  break;
                case 4: updateStudent();  break;
                case 5: running = false;  break;
                default: System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }
}
