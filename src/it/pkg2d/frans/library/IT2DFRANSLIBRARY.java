package it.pkg2d.frans.library;

import java.util.Scanner;

public class IT2DFRANSLIBRARY {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config dbConfig = new config(); 

        String response;
        boolean exitConfirmed = false;

        do {
            System.out.println("\n--------------------------");
            System.out.println("LIBRARY MANAGEMENT SYSTEM |");
            System.out.println("1. STUDENT PANEL          |");
            System.out.println("2. BOOK PANEL             |");
            System.out.println("3. BORROWED BOOK PANEL    |");
            System.out.println("4. REPORTS                |");
            System.out.println("5. EXIT                   |");
            System.out.println("--------------------------");

            System.out.print("Enter Selection (1-5): ");
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number (1-5): ");
                sc.next(); 
            }
            int action = sc.nextInt();
            sc.nextLine(); 

            switch (action) {
                case 1:
                    Students student = new Students();
                    student.sDetails();
                    break;
                case 2:
                    Books bookPanel = new Books();
                    bookPanel.bDetails();
                    break;
                case 3:
                    BorrowedBookInfo borrowedBookInfo = new BorrowedBookInfo();
                    borrowedBookInfo.bDetails(sc);
                    break;
                case 4:
                    Reports report = new Reports(dbConfig); 
                    report.showReportMenu(sc);  
                    break;
                case 5:
                    System.out.print("Are you sure you want to exit? (yes/no): ");
                    response = sc.nextLine().trim();
                    if (response.equalsIgnoreCase("yes")) {
                        exitConfirmed = true;
                        System.out.println("Exiting the Library Management System.");
                    } else {
                        System.out.println("Returning to main menu...");
                    }
                    break;
                default:
                    System.out.println("Invalid selection, please choose a valid option (1-5).");
                    break;
            }

            if (!exitConfirmed) {
                System.out.print("Do you want to continue in the Library Management System? (yes/no): ");
                response = sc.nextLine().trim();
                if (response.equalsIgnoreCase("no")) {
                    System.out.println("Exiting the Library Management System.");
                    exitConfirmed = true; 
                } else if (!response.equalsIgnoreCase("yes")) {
                    System.out.println("Invalid input. Please type 'yes' to continue or 'no' to exit.");
                }
            }

        } while (!exitConfirmed);
        sc.close(); 
    }
}
