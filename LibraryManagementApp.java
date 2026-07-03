import java.util.Scanner;

// ─── Book class ───
class Book {
    String title, author, isbn;
    boolean isAvailable;

    public Book(String title, String author, String isbn) {
        this.title  = title;
        this.author = author;
        this.isbn   = isbn;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return String.format("ISBN: %-12s | Title: %-30s | Author: %-20s | %s",
                isbn, title, author, isAvailable ? "Available" : "Checked Out");
    }
}

// ─── Library ───
public class LibraryManagementApp {
    static Book[] books = new Book[100];
    static int count = 0;
    static Scanner sc = new Scanner(System.in);

    public static void addBook() {
        sc.nextLine();
        System.out.print("Title: ");  String title  = sc.nextLine();
        System.out.print("Author: "); String author = sc.nextLine();
        System.out.print("ISBN: ");   String isbn   = sc.nextLine();
        books[count++] = new Book(title, author, isbn);
        System.out.println("Book added!");
    }

    public static void searchByTitle() {
        sc.nextLine();
        System.out.print("Enter title keyword: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (books[i].title.toLowerCase().contains(keyword)) {
                System.out.println(books[i]); found = true;
            }
        }
        if (!found) System.out.println("No books found.");
    }

    public static void searchByAuthor() {
        sc.nextLine();
        System.out.print("Enter author name: ");
        String author = sc.nextLine().toLowerCase();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (books[i].author.toLowerCase().contains(author)) {
                System.out.println(books[i]); found = true;
            }
        }
        if (!found) System.out.println("No books found.");
    }

    public static void displayAll() {
        if (count == 0) { System.out.println("Library is empty."); return; }
        System.out.println("\n===== Book Inventory =====");
        for (int i = 0; i < count; i++) System.out.println((i + 1) + ". " + books[i]);
    }

    public static void checkoutBook() {
        sc.nextLine();
        System.out.print("Enter ISBN to checkout: ");
        String isbn = sc.nextLine();
        for (int i = 0; i < count; i++) {
            if (books[i].isbn.equals(isbn)) {
                if (books[i].isAvailable) { books[i].isAvailable = false; System.out.println("Book checked out!"); }
                else System.out.println("Book not available.");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public static void returnBook() {
        sc.nextLine();
        System.out.print("Enter ISBN to return: ");
        String isbn = sc.nextLine();
        for (int i = 0; i < count; i++) {
            if (books[i].isbn.equals(isbn)) {
                books[i].isAvailable = true;
                System.out.println("Book returned. Thank you!"); return;
            }
        }
        System.out.println("Book not found.");
    }

    // Pre-load sample books
    static void loadSamples() {
        books[count++] = new Book("The Great Gatsby",    "F. Scott Fitzgerald", "ISBN001");
        books[count++] = new Book("To Kill a Mockingbird","Harper Lee",         "ISBN002");
        books[count++] = new Book("1984",                "George Orwell",       "ISBN003");
        books[count++] = new Book("Clean Code",          "Robert C. Martin",    "ISBN004");
    }

    public static void main(String[] args) {
        loadSamples();
        System.out.println("===== Library Management System =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Add Book      2. Display All");
            System.out.println("3. Search Title  4. Search Author");
            System.out.println("5. Checkout      6. Return  7. Exit");
            System.out.print("Choice: ");
            switch (sc.nextInt()) {
                case 1: addBook();       break;
                case 2: displayAll();    break;
                case 3: searchByTitle(); break;
                case 4: searchByAuthor();break;
                case 5: checkoutBook();  break;
                case 6: returnBook();    break;
                case 7: running = false; break;
                default: System.out.println("Invalid.");
            }
        }
        sc.close();
    }
}
