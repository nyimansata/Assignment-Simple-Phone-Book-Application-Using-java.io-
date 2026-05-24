import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String PHONE_FILE = "phone.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Simple Phone Book Application ===");
        ensureFileExists();

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    addContact();
                    break;
                case "2":
                    viewContacts();
                    break;
                case "3":
                    searchContact();
                    break;
                case "4":
                    deleteContact();
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        }
    }

    // Print the main menu options
    private static void printMenu() {
        System.out.println();
        System.out.println("1. Add Contact");
        System.out.println("2. View Contacts");
        System.out.println("3. Search Contact");
        System.out.println("4. Delete Contact");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    // Ensure the phone.txt file exists before any read/write operation
    private static void ensureFileExists() {
        File file = new File(PHONE_FILE);
        try {
            if (file.createNewFile()) {
                System.out.println("Created new contact storage file: " + PHONE_FILE);
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    // Add a contact and save it to phone.txt
    private static void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) {
            System.out.println("Phone number cannot be empty.");
            return;
        }

        List<String> contacts = readAllContacts();
        if (isDuplicatePhone(phone, contacts)) {
            System.out.println("This phone number already exists in the contact list.");
            return;
        }

        String newContact = name + " - " + phone;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PHONE_FILE, true))) {
            writer.write(newContact);
            writer.newLine();
            System.out.println("Contact added successfully.");
        } catch (IOException e) {
            System.out.println("Error saving contact: " + e.getMessage());
        }
    }

    // Display all contacts from phone.txt
    private static void viewContacts() {
        List<String> contacts = readAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        System.out.println("\nSaved Contacts:");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
    }

    // Search for contacts by name or phone number
    private static void searchContact() {
        System.out.print("Enter name or phone to search: ");
        String query = scanner.nextLine().trim().toLowerCase();
        if (query.isEmpty()) {
            System.out.println("Search query cannot be empty.");
            return;
        }

        List<String> contacts = readAllContacts();
        List<String> found = new ArrayList<>();
        for (String contact : contacts) {
            if (contact.toLowerCase().contains(query)) {
                found.add(contact);
            }
        }

        if (found.isEmpty()) {
            System.out.println("No matching contacts found.");
        } else {
            System.out.println("\nSearch results:");
            for (String contact : found) {
                System.out.println(contact);
            }
        }
    }

    // Delete a contact and update phone.txt
    private static void deleteContact() {
        List<String> contacts = readAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts to delete.");
            return;
        }

        System.out.println("\nSelect contact number to delete:");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
        System.out.print("Enter number: ");

        String input = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= contacts.size()) {
                System.out.println("Invalid contact number.");
                return;
            }
            String removedContact = contacts.remove(index);
            writeAllContacts(contacts);
            System.out.println("Deleted contact: " + removedContact);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    // Read all contacts from the file into a list
    private static List<String> readAllContacts() {
        List<String> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PHONE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    contacts.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading contacts: " + e.getMessage());
        }
        return contacts;
    }

    // Write all contacts back to phone.txt after deletion or update
    private static void writeAllContacts(List<String> contacts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PHONE_FILE, false))) {
            for (String contact : contacts) {
                writer.write(contact);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating contacts: " + e.getMessage());
        }
    }

    // Check for duplicate phone numbers before saving new contact
    private static boolean isDuplicatePhone(String phone, List<String> contacts) {
        for (String contact : contacts) {
            String[] parts = contact.split(" - ", 2);
            if (parts.length == 2 && parts[1].equals(phone)) {
                return true;
            }
        }
        return false;
    }
}
