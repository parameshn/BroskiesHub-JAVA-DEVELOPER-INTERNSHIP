/* Key Concepts: Understand Java OOP basics like classes, objects, methods, and using ArrayList for
 storing data.
 Steps:
 Create a Student class with id, name, and grade.
 Add a constructor and toString() method for display.
 Create an ArrayList<Student> in the main class.
 Write addStudent() method to add a new student.
 Write removeStudent() method to delete by ID.
 Write displayStudents() method to show a l students.
 Use a loop or menu for user input (add/remove/list).
 Run and test with sample data. */


import java.util.ArrayList;
import java.util.Scanner;

// Student class demonstrating OOP concepts
class Student {
    private int id;
    private String name;
    private double grade;

    // Constructor
    public Student(int id, String name, double grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    // toString method for displaying student information
    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Grade: %.2f", id, name, grade);
    }
}

// Main class for Student Management System
public class StudentManagementSystem {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextId = 1;

    public static void main(String[] args) {
        System.out.println("=== Student Management System ===");

        while (true) {
            displayMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    removeStudent();
                    break;
                case 3:
                    displayStudents();
                    break;
                case 4:
                    searchStudent();
                    break;
                case 5:
                    updateStudent();
                    break;
                case 6:
                    System.out.println("Thank you for using Student Management System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // Display menu options
    private static void displayMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Add Student");
        System.out.println("2. Remove Student");
        System.out.println("3. Display All Students");
        System.out.println("4. Search Student");
        System.out.println("5. Update Student");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    // Get user's menu choice
    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Add a new student
    private static void addStudent() {
        System.out.println("\n--- Add New Student ---");

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Enter student grade: ");
        try {
            double grade = Double.parseDouble(scanner.nextLine());

            if (grade < 0 || grade > 100) {
                System.out.println("Grade must be between 0 and 100!");
                return;
            }

            Student newStudent = new Student(nextId++, name, grade);
            students.add(newStudent);
            System.out.println("Student added successfully!");
            System.out.println("Added: " + newStudent);

        } catch (NumberFormatException e) {
            System.out.println("Invalid grade format! Please enter a number.");
        }
    }

    // Remove a student by ID
    private static void removeStudent() {
        System.out.println("\n--- Remove Student ---");

        if (students.isEmpty()) {
            System.out.println("No students to remove!");
            return;
        }

        displayStudents();
        System.out.print("Enter student ID to remove: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            boolean found = false;

            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId() == id) {
                    Student removedStudent = students.remove(i);
                    System.out.println("Student removed successfully!");
                    System.out.println("Removed: " + removedStudent);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Student with ID " + id + " not found!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format! Please enter a number.");
        }
    }

    // Display all students
    private static void displayStudents() {
        System.out.println("\n--- All Students ---");

        if (students.isEmpty()) {
            System.out.println("No students found!");
            return;
        }

        System.out.println("Total students: " + students.size());
        System.out.println("------------------------");

        for (Student student : students) {
            System.out.println(student);
        }

        // Calculate and display average grade
        double totalGrades = 0;
        for (Student student : students) {
            totalGrades += student.getGrade();
        }
        double averageGrade = totalGrades / students.size();
        System.out.println("------------------------");
        System.out.printf("Average Grade: %.2f%n", averageGrade);
    }

    // Search for a student by ID
    private static void searchStudent() {
        System.out.println("\n--- Search Student ---");
        System.out.print("Enter student ID to search: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            boolean found = false;

            for (Student student : students) {
                if (student.getId() == id) {
                    System.out.println("Student found:");
                    System.out.println(student);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Student with ID " + id + " not found!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format! Please enter a number.");
        }
    }

    // Update student information
    private static void updateStudent() {
        System.out.println("\n--- Update Student ---");

        if (students.isEmpty()) {
            System.out.println("No students to update!");
            return;
        }

        displayStudents();
        System.out.print("Enter student ID to update: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Student studentToUpdate = null;

            for (Student student : students) {
                if (student.getId() == id) {
                    studentToUpdate = student;
                    break;
                }
            }

            if (studentToUpdate == null) {
                System.out.println("Student with ID " + id + " not found!");
                return;
            }

            System.out.println("Current student info: " + studentToUpdate);
            System.out.println("What would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Grade");
            System.out.println("3. Both");
            System.out.print("Enter choice: ");

            int updateChoice = Integer.parseInt(scanner.nextLine());

            switch (updateChoice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        studentToUpdate.setName(newName);
                        System.out.println("Name updated successfully!");
                    }
                    break;
                case 2:
                    System.out.print("Enter new grade: ");
                    double newGrade = Double.parseDouble(scanner.nextLine());
                    if (newGrade >= 0 && newGrade <= 100) {
                        studentToUpdate.setGrade(newGrade);
                        System.out.println("Grade updated successfully!");
                    } else {
                        System.out.println("Grade must be between 0 and 100!");
                    }
                    break;
                case 3:
                    System.out.print("Enter new name: ");
                    String newName2 = scanner.nextLine().trim();
                    System.out.print("Enter new grade: ");
                    double newGrade2 = Double.parseDouble(scanner.nextLine());

                    if (!newName2.isEmpty() && newGrade2 >= 0 && newGrade2 <= 100) {
                        studentToUpdate.setName(newName2);
                        studentToUpdate.setGrade(newGrade2);
                        System.out.println("Student information updated successfully!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            System.out.println("Updated student info: " + studentToUpdate);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input format! Please enter valid numbers.");
        }
    }
}