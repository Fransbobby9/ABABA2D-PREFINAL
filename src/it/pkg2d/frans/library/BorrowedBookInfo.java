package it.pkg2d.frans.library;

import java.util.Scanner;

public class BorrowedBookInfo {

    private int getValidIntInput(Scanner sc, String prompt) {
        int input;
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid number: ");
            sc.next(); 
        }
        input = sc.nextInt();
        return input;
    }

    private String getValidDateInput(Scanner sc, String prompt) {
        String date;
        System.out.print(prompt);
        date = sc.next();
        while (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.print("Invalid date format. Please enter a valid date (YYYY-MM-DD): ");
            date = sc.next();
        }
        return date;
    }

    public void bDetails(Scanner sc) {
        String response;

        do {
            System.out.println("\n--------------------------");
            System.out.println("BORROWED BOOK PANEL        |");
            System.out.println("1. ADD BORROWING RECORD    |");
            System.out.println("2. VIEW BORROWED BOOKS     |");
            System.out.println("3. UPDATE BORROWING        |");
            System.out.println("4. DELETE BORROWING RECORD |");
            System.out.println("5. EXIT                    |");
            System.out.println("--------------------------");

            int action = getValidIntInput(sc, "Enter Selection (1-5): ");
            while (action < 1 || action > 5) {
                System.out.print("Invalid selection, Try Again: ");
                action = getValidIntInput(sc, "Enter Selection (1-5): ");
            }

            switch (action) {
                case 1:
                    addBorrowingRecord(sc);
                    viewBorrowedBooks();
                    break;
                case 2:
                    viewBorrowedBooks();
                    break;
                case 3:
                    viewBorrowedBooks();
                    updateBorrowing(sc);
                    viewBorrowedBooks();
                    break;
                case 4:
                    viewBorrowedBooks();
                    deleteBorrowing(sc);
                    viewBorrowedBooks();
                    break;
                case 5:
                    System.out.println("Exiting Borrowed Book Panel.");
                    break;
            }

            System.out.print("Do you want to continue in Borrowed Book Panel? (yes/no): ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));
    }

    public void addBorrowingRecord(Scanner sc) {
        try {
            int studentId = getValidIntInput(sc, "Enter Student ID: ");
            int bookId = getValidIntInput(sc, "Enter Book ID: ");
            String dueDate = getValidDateInput(sc, "Enter Due Date (YYYY-MM-DD): ");

            String sql = "INSERT INTO BorrowedBooks (StudentID, BookID, DueDate) VALUES (?, ?, ?)";
            config conf = new config();
            conf.addRecord(sql, studentId, bookId, dueDate);

            System.out.println("Borrowing record added successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while adding borrowing record: " + e.getMessage());
        }
    }

    public void viewBorrowedBooks() {
        try {
            String qry = "SELECT TransactionID, StudentID, BookID, BorrowedDate, DueDate FROM BorrowedBooks";
            String[] headers = {"Transaction ID", "Student ID", "Book ID", "Borrowed Date", "Due Date"};
            String[] columns = {"TransactionID", "StudentID", "BookID", "BorrowedDate", "DueDate"};

            config conf = new config();
            conf.viewRecords(qry, headers, columns);
        } catch (Exception e) {
            System.out.println("Error occurred while viewing borrowed books: " + e.getMessage());
        }
    }

    public void updateBorrowing(Scanner sc) {
        try {
            config conf = new config();

            int transactionId = getValidIntInput(sc, "Enter Transaction ID to update: ");
            while (conf.getSingleValue("SELECT TransactionID FROM BorrowedBooks WHERE TransactionID = ?", transactionId) == 0) {
                System.out.print("Selected Transaction ID doesn't exist. Try again: ");
                transactionId = getValidIntInput(sc, "Enter Transaction ID to update: ");
            }

            String dueDate = getValidDateInput(sc, "Enter New Due Date (YYYY-MM-DD): ");
            String sql = "UPDATE BorrowedBooks SET DueDate = ? WHERE TransactionID = ?";
            conf.updateRecord(sql, dueDate, transactionId);

            System.out.println("Borrowing record updated successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while updating borrowing record: " + e.getMessage());
        }
    }

    public void deleteBorrowing(Scanner sc) {
        try {
            config conf = new config();

            int transactionId = getValidIntInput(sc, "Enter Transaction ID to delete: ");
            while (conf.getSingleValue("SELECT TransactionID FROM BorrowedBooks WHERE TransactionID = ?", transactionId) == 0) {
                System.out.print("Selected Transaction ID doesn't exist. Try again: ");
                transactionId = getValidIntInput(sc, "Enter Transaction ID to delete: ");
            }

            String sqlDelete = "DELETE FROM BorrowedBooks WHERE TransactionID = ?";
            conf.deleteRecord(sqlDelete, transactionId);

            System.out.println("Borrowing record deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while deleting borrowing record: " + e.getMessage());
        }
    }
}
