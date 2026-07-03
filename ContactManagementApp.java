import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

// ─── Contact class ───
class Contact {
    String name, phone, email;

    public Contact(String name, String phone, String email) {
        this.name  = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("Name: %-20s | Phone: %-15s | Email: %s", name, phone, email);
    }
}

// ─── Contact App ───
public class ContactManagementApp {
    static Vector<Contact> contacts = new Vector<>();
    static Scanner sc = new Scanner(System.in);

    public static void addContact() {
        sc.nextLine();
        System.out.print("Name:  "); String name  = sc.nextLine();
        System.out.print("Phone: "); String phone = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        contacts.add(new Contact(name, phone, email));
        System.out.println("Contact saved!");
    }

    public static void displayAll() {
        if (contacts.isEmpty()) { System.out.println("No contacts."); return; }
        System.out.println("\n===== Contacts =====");
        for (int i = 0; i < contacts.size(); i++)
            System.out.println((i + 1) + ". " + contacts.get(i));
    }

    public static void searchContact() {
        sc.nextLine();
        System.out.print("Search (name/phone): ");
        String q = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Contact c : contacts) {
            if (c.name.toLowerCase().contains(q) || c.phone.contains(q)) {
                System.out.println(c); found = true;
            }
        }
        if (!found) System.out.println("No match found.");
    }

    public static void sortContacts() {
        contacts.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
        System.out.println("Contacts sorted by name.");
        displayAll();
    }

    public static void deleteContact() {
        sc.nextLine();
        System.out.print("Enter name to delete: ");
        String name = sc.nextLine();
        boolean removed = contacts.removeIf(c -> c.name.equalsIgnoreCase(name));
        System.out.println(removed ? "Contact deleted." : "Contact not found.");
    }

    public static void exportContacts() {
        System.out.println("\n===== Exported Contacts (CSV) =====");
        System.out.println("Name,Phone,Email");
        for (Contact c : contacts)
            System.out.printf("%s,%s,%s%n", c.name, c.phone, c.email);
    }

    // Pre-load sample contacts
    static void loadSamples() {
        contacts.add(new Contact("Alice Sharma",  "9876543210", "alice@gmail.com"));
        contacts.add(new Contact("Bob Mehta",     "8765432109", "bob@yahoo.com"));
        contacts.add(new Contact("Charlie Patel", "7654321098", "charlie@hotmail.com"));
    }

    public static void main(String[] args) {
        loadSamples();
        System.out.println("===== Contact Management App =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Add    2. Display  3. Search");
            System.out.println("4. Sort   5. Delete   6. Export  7. Exit");
            System.out.print("Choice: ");
            switch (sc.nextInt()) {
                case 1: addContact();    break;
                case 2: displayAll();    break;
                case 3: searchContact(); break;
                case 4: sortContacts();  break;
                case 5: deleteContact(); break;
                case 6: exportContacts();break;
                case 7: running = false; break;
                default: System.out.println("Invalid.");
            }
        }
        sc.close();
    }
}
