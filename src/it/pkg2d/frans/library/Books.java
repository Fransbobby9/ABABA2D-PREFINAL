package it.pkg2d.frans.library;

import java.util.Scanner;

public class Books {

    // Helper method for input validation
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

            System.out.print("Enter Selection: ");
            int action = sc.nextInt();
            sc.nextLine(); 

            while (action < 1 || action > 5) {
                System.out.print("Invalid selection, Try Again: ");
                while (!sc.hasNextInt()) {
                    System.out.print("Please enter a valid number: ");
                    sc.next(); 
                }
                action = sc.nextInt();
                sc.nextLine();  
            }

            Books bk = new Books();

            switch (action) {
                case 1:
                    bk.addBook(sc);
                    bk.viewBooks();
                    break;
                case 2:
                    bk.viewBooks();
                    break;
                case 3:
                    bk.viewBooks();
                    bk.updateBook(sc);
                    bk.viewBooks();
                    break;
                case 4:
                    bk.viewBooks();
                    bk.deleteBook(sc);
                    bk.viewBooks();
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
            System.out.print("How many books do you want to add? ");
            int numberOfBooks = sc.nextInt();
            sc.nextLine();  

            for (int i = 0; i < numberOfBooks; i++) {
                System.out.println("Enter details for Book " + (i + 1));

                String title = getValidInput(sc, "Book Title: ", null, "Title cannot be empty.");
                String author = getValidInput(sc, "Book Author: ", null, "Author cannot be empty.");
                String genre = getValidInput(sc, "Book Genre: ", null, "Genre cannot be empty.");
                String isbn = getValidInput(sc, "Book ISBN (13 digits): ", "[0-9]{13}", "Invalid ISBN, Please enter 13 digits.");

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

            System.out.print("Enter Book ID to update: ");
            int id = sc.nextInt();
            sc.nextLine(); 

            while (conf.getSingleValue("SELECT BookID FROM Books WHERE BookID = ?", id) == 0) {
                System.out.print("Selected ID doesn't exist. Try again: ");
                id = sc.nextInt();
                sc.nextLine(); 
            }

            String title = getValidInput(sc, "Book Title: ", null, "Title cannot be empty.");
            String author = getValidInput(sc, "Book Author: ", null, "Author cannot be empty.");
            String genre = getValidInput(sc, "Book Genre: ", null, "Genre cannot be empty.");
            String isbn = getValidInput(sc, "Book ISBN (13 digits): ", "[0-9]{13}", "Invalid ISBN, Please enter 13 digits.");
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

            System.out.print("Enter Book ID to delete: ");
            int id = sc.nextInt();
            sc.nextLine();  

            while (conf.getSingleValue("SELECT BookID FROM Books WHERE BookID = ?", id) == 0) {
                System.out.print("Selected ID doesn't exist. Try again: ");
                id = sc.nextInt();
                sc.nextLine();  
            }

            String sqlDelete = "DELETE FROM Books WHERE BookID = ?";
            conf.deleteRecord(sqlDelete, id);

            System.out.println("Book record deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error occurred while deleting book: " + e.getMessage());
        }
    }
}
