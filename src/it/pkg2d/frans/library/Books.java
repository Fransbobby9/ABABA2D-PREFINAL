package it.pkg2d.frans.library;

import java.util.Scanner;

public class Books {

    private String getValidInput(Scanner sc, String prompt, String validationPattern, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine();
            if (input.isEmpty() || (validationPattern != null && !input.matches(validationPattern))) {
                System.out.println(errorMessage);
            }
        } while (input.isEmpty() || (validationPattern != null && !input.matches(validationPattern)));
        return input;
    }

    private int getValidIntInput(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid number: ");
            sc.next(); 
        }
        int input = sc.nextInt();
        sc.nextLine(); 
        return input;
    }

    public void bDetails() {
        Scanner sc = new Scanner(System.in);
        String response;

        do {
            System.out.println("\n--------------------------");
            System.out.println("BOOK PANEL                |");
            System.out.println("1. ADD BOOK               |");
            System.out.println("2. VIEW BOOKS             |");
            System.out.println("3. UPDATE BOOK            |");
            System.out.println("4. DELETE BOOK            |");
            System.out.println("5. EXIT                   |");
            System.out.println("--------------------------");

            int action = getValidIntInput(sc, "Enter Selection (1-5): ");
            while (action < 1 || action > 5) {
                System.out.print("Invalid selection. Try again: ");
                action = getValidIntInput(sc, "Enter Selection (1-5): ");
            }

            switch (action) {
                case 1:
                    addBook(sc);
                    viewBooks();
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    viewBooks();
                    updateBook(sc);
                    viewBooks();
                    break;
                case 4:
                    viewBooks();
                    deleteBook(sc);
                    viewBooks();
                    break;
                case 5:
                    System.out.println("Exiting Book Panel.");
                    break;
            }

            System.out.print("Do you want to continue in Book Panel? (yes/no): ");
            response = sc.nextLine();
        } while (response.equalsIgnoreCase("yes"));
    }

    public void addBook(Scanner sc) {
        try {
            int numberOfBooks = getValidIntInput(sc, "How many books do you want to add? ");

            for (int i = 0; i < numberOfBooks; i++) {
                System.out.println("Enter details for Book " + (i + 1));

                String title = getValidInput(sc, "Book Title: ", null, "Title cannot be empty.");
                String author = getValidInput(sc, "Book Author: ", null, "Author cannot be empty.");
                String genre = getValidInput(sc, "Book Genre: ", null, "Genre cannot be empty.");
                String isbn = getValidInput(sc, "Book ISBN (13 digits): ", "\\d{13}", "Invalid ISBN, please enter 13 digits.");

                String sql = "INSERT INTO Books (Title, Author, Genre, ISBN, Status) VALUES (?, ?, ?, ?, 'Available')";
                config conf = new config();
                conf.addRecord(sql, title, author, genre, isbn);

                System.out.println("Book " + (i + 1) + " added successfully!");
            }
            System.out.println("All books have been added.");
        } catch (Exception e) {
            System.out.println("Error occurred while adding books: " + e.getMessage());
        }
    }

    public void viewBooks() {
        try {
            String qry = "SELECT BookID, Title, Author, Genre, ISBN, Status FROM Books";
            String[] headers = {"ID", "Title", "Author", "Genre", "ISBN", "Status"};
            String[] columns = {"BookID", "Title", "Author", "Genre", "ISBN", "Status"};

            config conf = new config();
            conf.viewRecords(qry, headers, columns);
        } catch (Exception e) {
            System.out.println("Error occurred while viewing books: " + e.getMessage());
        }
    }

    public void updateBook(Scanner sc) {
        try {
            config conf = new config();
            int id = getValidIntInput(sc, "Enter Book ID to update: ");
            
            while (conf.getSingleValue("SELECT BookID FROM Books WHERE BookID = ?", id) == 0) {
                System.out.print("Selected ID doesn't exist. Try again: ");
                id = getValidIntInput(sc, "Enter Book ID to update: ");
            }

            String title = getValidInput(sc, "Book Title: ", null, "Title cannot be empty.");
            String author = getValidInput(sc, "Book Author: ", null, "Author cannot be empty.");
            String genre = getValidInput(sc, "Book Genre: ", null, "Genre cannot be empty.");
            String isbn = getValidInput(sc, "Book ISBN (13 digits): ", "\\d{13}", "Invalid ISBN, please enter 13 digits.");
            String status = getValidInput(sc, "Book Status (Available/Borrowed): ", "(Available|Borrowed)", "Invalid status, please enter 'Available' or 'Borrowed'.");

            String qry = "UPDATE Books SET Title = ?, Author = ?, Genre = ?, ISBN = ?, Status = ? WHERE BookID = ?";
            conf.updateRecord(qry, title, author, genre, isbn, status, id);

            System.out.println("Book record updated successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while updating book: " + e.getMessage());
        }
    }

    public void deleteBook(Scanner sc) {
        try {
            config conf = new config();
            int id = getValidIntInput(sc, "Enter Book ID to delete: ");

            while (conf.getSingleValue("SELECT BookID FROM Books WHERE BookID = ?", id) == 0) {
                System.out.print("Selected ID doesn't exist. Try again: ");
                id = getValidIntInput(sc, "Enter Book ID to delete: ");
            }

            String sqlDelete = "DELETE FROM Books WHERE BookID = ?";
            conf.deleteRecord(sqlDelete, id);

            System.out.println("Book record deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while deleting book: " + e.getMessage());
        }
    }
}
