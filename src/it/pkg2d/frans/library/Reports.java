package it.pkg2d.frans.library;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reports {
    private config dbConfig;

    public Reports(config dbConfig) {
        this.dbConfig = dbConfig;
    }

    public void showReportMenu(Scanner sc) {
        String response;

        do {
            System.out.println("\n--------------------------");
            System.out.println("REPORTS MENU              |");
            System.out.println("1. VIEW ALL BOOKS          |");
            System.out.println("2. VIEW ALL BORROWED BOOKS |");
            System.out.println("3. VIEW AVAILABLE BOOKS    |");
            System.out.println("4. EXIT                    |");
            System.out.println("--------------------------");

            int action = getValidIntInput(sc, 1, 4);

            switch (action) {
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    viewAllBorrowedBooks();
                    break;
                case 3:
                    viewAvailableBooks();
                    break;
                case 4:
                    System.out.println("Exiting Reports Menu.");
                    return;
            }

            System.out.print("Do you want to continue in Reports Menu? (yes/no): ");
            response = sc.nextLine().trim();

            if (response.equalsIgnoreCase("no")) {
                System.out.println("Exiting Reports Menu.");
                return;
            }

        } while (response.equalsIgnoreCase("yes"));
    }

    private void viewAllBooks() {
        String sqlQuery = "SELECT BookID, Title, Author, Genre, ISBN, Status FROM Books";
        String[] headers = {"BookID", "Title", "Author", "Genre", "ISBN", "Status"};
        String[] columns = {"BookID", "Title", "Author", "Genre", "ISBN", "Status"};
        executeQueryAndDisplayResults(sqlQuery, headers,columns);
    }

    private void viewAllBorrowedBooks() {
        String sqlQuery = "SELECT bb.TransactionID, " +
                          "s.s_name AS StudentName, " +  
                          "bb.BookID, " +
                          "bb.BorrowedDate, " +
                          "bb.DueDate " +
                          "FROM BorrowedBooks bb " +
                          "JOIN Students s ON bb.StudentID = s.s_id"; 

        String[] headers = {"TransactionID", "Student Name", "BookID", "BorrowedDate", "DueDate"};
        String[] columns = {"TransactionID", "StudentName", "BookID", "BorrowedDate", "DueDate"};

        executeQueryAndDisplayResults(sqlQuery, headers, columns);
    }

    private void viewAvailableBooks() {
        String sqlQuery = "SELECT BookID, Title, Author, Genre, ISBN FROM Books WHERE Status = 'Available'";
        String[] headers = {"BookID", "Title", "Author", "Genre", "ISBN"};
        String[] columns = {"BookID", "Title", "Author", "Genre", "ISBN"};
        executeQueryAndDisplayResults(sqlQuery, headers, columns);
    }

    private void executeQueryAndDisplayResults(String query, String[] headers, String[] columns) {
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("No records found.");
                return;
            }

            printTableHeader(headers);

            while (rs.next()) {
                for (String column : columns) {
                    System.out.printf("%-25s", rs.getString(column));
                }
                System.out.println();
            }
            System.out.println("--------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }

    private void printTableHeader(String[] headers) {
        System.out.println("--------------------------------------------------------------------------------------------");
        for (String header : headers) {
            System.out.printf("%-25s", header);
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    private int getValidIntInput(Scanner sc, int min, int max) {
        int input = -1;
        boolean valid = false;

        while (!valid) {
            try {
                input = sc.nextInt();
                sc.nextLine(); 

                if (input >= min && input <= max) {
                    valid = true;
                } else {
                    System.out.print("Invalid selection, try again (" + min + "-" + max + "): ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number (" + min + "-" + max + "): ");
                sc.nextLine(); 
            }
        }
        return input;
    }
}
