/*
 *  Key Concepts:Work with String class, loops, StringBuilder, and conditional statements.
 Steps:
 1.Create Employee class with name, age, salary.
 2.Add a constructor and toString() for easy display.
 3.Store multiple employees in an ArrayList.
 4.Use Colections.sort() with Comparator for sorting by
 salary (desc).
 5.Sort by name (asc) using another Comparator.
 6.Print sorted lists.
 */


import java.util.*;

// Employee class with name, age, and salary
class Employee {
    private String name;
    private int age;
    private double salary;

    // Constructor
    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    // toString method for easy display
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Employee{");
        sb.append("name='").append(name).append("'");
        sb.append(", age=").append(age);
        sb.append(", salary=$").append(String.format("%.2f", salary));
        sb.append("}");
        return sb.toString();
    }
}

public class EmployeeManagementSystem {
    public static void main(String[] args) {
        // Create ArrayList to store employees
        ArrayList<Employee> employees = new ArrayList<>();

        // Add multiple employees using loops and conditional statements
        String[] names = { "Alice Johnson", "Bob Smith", "Charlie Brown", "Diana Prince", "Eve Adams" };
        int[] ages = { 28, 35, 42, 31, 26 };
        double[] salaries = { 75000.50, 65000.75, 85000.25, 92000.00, 58000.80 };

        // Using loop to create and add employees
        for (int i = 0; i < names.length; i++) {
            // Conditional statement to validate data
            if (names[i] != null && !names[i].trim().isEmpty() && ages[i] > 0 && salaries[i] > 0) {
                employees.add(new Employee(names[i], ages[i], salaries[i]));
            }
        }

        // Display original list
        System.out.println("=== ORIGINAL EMPLOYEE LIST ===");
        displayEmployees(employees);

        // Sort by salary (descending order) using Comparator
        System.out.println("\n=== SORTED BY SALARY (DESCENDING) ===");
        Collections.sort(employees, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return Double.compare(e2.getSalary(), e1.getSalary()); // Descending order
            }
        });
        displayEmployees(employees);

        // Sort by name (ascending order) using Lambda expression Comparator
        System.out.println("\n=== SORTED BY NAME (ASCENDING) ===");
        Collections.sort(employees, (e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()));
        displayEmployees(employees);

        // Additional sorting examples using method references
        System.out.println("\n=== SORTED BY AGE (ASCENDING) ===");
        Collections.sort(employees, Comparator.comparingInt(Employee::getAge));
        displayEmployees(employees);

        // Demonstrate String operations and StringBuilder
        demonstrateStringOperations(employees);

        // Filter employees using loops and conditionals
        filterEmployees(employees);
    }

    // Method to display employees using enhanced for loop
    public static void displayEmployees(ArrayList<Employee> employees) {
        int count = 1;
        for (Employee emp : employees) {
            System.out.println(count + ". " + emp.toString());
            count++;
        }
    }

    // Demonstrate string operations and StringBuilder
    public static void demonstrateStringOperations(ArrayList<Employee> employees) {
        System.out.println("\n=== STRING OPERATIONS DEMONSTRATION ===");

        StringBuilder report = new StringBuilder();
        report.append("EMPLOYEE SUMMARY REPORT\n");
        report.append("=======================\n");

        double totalSalary = 0;
        int employeeCount = 0;

        for (Employee emp : employees) {
            // String operations
            String name = emp.getName();
            String firstName = name.substring(0, name.indexOf(" "));
            String lastName = name.substring(name.indexOf(" ") + 1);

            // Conditional statements for categorization
            String salaryCategory;
            if (emp.getSalary() >= 80000) {
                salaryCategory = "High";
            } else if (emp.getSalary() >= 65000) {
                salaryCategory = "Medium";
            } else {
                salaryCategory = "Entry";
            }

            // StringBuilder operations
            report.append(String.format("%-15s %-15s Age: %-3d Salary: $%-10.2f Category: %s%n",
                    firstName, lastName, emp.getAge(), emp.getSalary(), salaryCategory));

            totalSalary += emp.getSalary();
            employeeCount++;
        }

        report.append("=======================\n");
        report.append(String.format("Total Employees: %d%n", employeeCount));
        report.append(String.format("Average Salary: $%.2f%n", totalSalary / employeeCount));

        System.out.println(report.toString());
    }

    // Filter employees based on conditions using loops and conditionals
    public static void filterEmployees(ArrayList<Employee> employees) {
        System.out.println("\n=== FILTERED EMPLOYEES ===");

        StringBuilder highEarnersReport = new StringBuilder();
        highEarnersReport.append("High Earners (Salary >= $80,000):\n");

        int highEarnerCount = 0;

        // Using traditional for loop with conditional statements
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);

            // Conditional filtering
            if (emp.getSalary() >= 80000.0) {
                highEarnersReport.append("• ").append(emp.getName())
                        .append(" - $").append(String.format("%.2f", emp.getSalary()))
                        .append("\n");
                highEarnerCount++;
            }
        }

        if (highEarnerCount == 0) {
            highEarnersReport.append("No employees found with salary >= $80,000\n");
        } else {
            highEarnersReport.insert(0, String.format("Found %d high earners:\n", highEarnerCount));
        }

        System.out.println(highEarnersReport.toString());

        // Filter young employees using enhanced for loop
        System.out.println("Young Employees (Age <= 30):");
        boolean foundYoung = false;
        for (Employee emp : employees) {
            if (emp.getAge() <= 30) {
                System.out.println("• " + emp.getName() + " (Age: " + emp.getAge() + ")");
                foundYoung = true;
            }
        }

        if (!foundYoung) {
            System.out.println("No employees found with age <= 30");
        }
    }
}