package it.pkg2d.frans.library;

import java.util.Scanner;

public class Students {

    private int getValidIntInput(Scanner sc, String prompt) {
        int input;
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid number: ");
            sc.next(); 
        }
        input = sc.nextInt();
        sc.nextLine(); 
        return input;
    }

    public void sDetails() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n--------------------------");
            System.out.println("STUDENT PANEL              |");
            System.out.println("1. ADD STUDENT             |");
            System.out.println("2. VIEW STUDENTS           |");
            System.out.println("3. UPDATE STUDENT          |");
            System.out.println("4. DELETE STUDENT          |");
            System.out.println("5. EXIT                    |");
            System.out.println("--------------------------");

            int action = getValidIntInput(sc, "Enter Selection (1-5): ");
            while (action < 1 || action > 5) {
                System.out.print("Invalid selection, Try Again: ");
                action = getValidIntInput(sc, "Enter Selection (1-5): ");
            }

            switch (action) {
                case 1:
                    addStudent(sc);
                    viewStudents();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    updateStudent(sc);
                    viewStudents();
                    break;
                case 4:
                    deleteStudent(sc);
                    viewStudents();
                    break;
                case 5:
                    System.out.println("Exiting Student Panel.");
                    break;
            }

            System.out.print("Do you want to continue in Student Panel? (yes/no): ");
            response = sc.nextLine();
        } while(response.equalsIgnoreCase("yes"));
    }

    public void addStudent(Scanner sc) {
        int numberOfStudents = getValidIntInput(sc, "How many students do you want to add? ");
        
        for (int i = 0; i < numberOfStudents; i++) {
            System.out.println("Enter details for Student " + (i + 1));

            String s_name = getValidatedInput(sc, "Student Name: ", true);
            String s_class = getValidatedInput(sc, "Class: ", true);
            String s_email = getValidatedInput(sc, "Email: ", false);
            String s_phone = getValidatedInput(sc, "Phone Number: ", false);

            String sql = "INSERT INTO Students (s_name, s_class, s_email, s_phone) VALUES (?, ?, ?, ?)";
            config conf = new config();
            conf.addRecord(sql, s_name, s_class, s_email, s_phone);

            System.out.println("Student " + (i + 1) + " added successfully!");
        }
        System.out.println("All students have been added.");
    }

    public void viewStudents() {
        String qry = "SELECT s_id, s_name, s_class, s_email, s_phone FROM Students";
        String[] headers = {"ID", "Name", "Class", "Email", "Phone"};
        String[] columns = {"s_id", "s_name", "s_class", "s_email", "s_phone"};

        config conf = new config();
        conf.viewRecords(qry, headers, columns);
    }

    public void updateStudent(Scanner sc) {
        config conf = new config();

        int id = getValidIntInput(sc, "Enter Student ID to update: ");
        while (conf.getSingleValue("SELECT s_id FROM Students WHERE s_id = ?", id) == 0) {
            System.out.print("Selected ID doesn't exist. Try again: ");
            id = getValidIntInput(sc, "Enter Student ID to update: ");
        }

        String s_name = getValidatedInput(sc, "Student Name: ", true);
        String s_class = getValidatedInput(sc, "Class: ", true);
        String s_email = getValidatedInput(sc, "Email: ", false);
        String s_phone = getValidatedInput(sc, "Phone Number: ", false);

        String qry = "UPDATE Students SET s_name = ?, s_class = ?, s_email = ?, s_phone = ? WHERE s_id = ?";
        conf.updateRecord(qry, s_name, s_class, s_email, s_phone, id);

        System.out.println("Student record updated successfully!");
    }

    public void deleteStudent(Scanner sc) {
        config conf = new config();

        int id = getValidIntInput(sc, "Enter Student ID to delete: ");
        while (conf.getSingleValue("SELECT s_id FROM Students WHERE s_id = ?", id) == 0) {
            System.out.print("Selected ID doesn't exist. Try again: ");
            id = getValidIntInput(sc, "Enter Student ID to delete: ");
        }

        String sqlDelete = "DELETE FROM Students WHERE s_id = ?";
        conf.deleteRecord(sqlDelete, id);

        System.out.println("Student record deleted successfully!");
    }

    private String getValidatedInput(Scanner sc, String prompt, boolean isRequired) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine();
            if (isRequired && input.isEmpty()) {
                System.out.println("This field cannot be empty.");
            } else if (!isRequired && input.isEmpty()) {
                break;  
            } else if (prompt.equals("Email: ") && !input.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Please enter a valid email address.");
            } else if (prompt.equals("Phone Number: ") && !input.matches("\\d{10}")) {
                System.out.println("Phone number must be 10 digits.");
            }
        } while (isRequired && input.isEmpty() || (prompt.equals("Email: ") && !input.matches("^[A-Za-z0-9+_.-]+@(.+)$")) || (prompt.equals("Phone Number: ") && !input.matches("\\d{10}")));
        return input;
    }
}
