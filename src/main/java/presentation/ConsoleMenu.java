package presentation;

import common.ValidationException;
import domain.model.*;
import domain.service.Library;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Function;

/**
 * Console based user interface for interacting with the {@link Library}.
 */
public class ConsoleMenu {

    private final Library library;
    private final Scanner scanner = new Scanner(System.in); // Creates scanner object to allow input

    public ConsoleMenu(Library library) {
        this.library = library; // Loads the library
    }

    /**
     * Initiates the main menu loop and processes user inputs until they choose to exit.
     */
    public void run() {
        boolean running = true;
        while (running) {
            // Display the console menu
            System.out.println();
            System.out.println("---Library System---\n");
            System.out.println("""
                    Welcome to the Library Management System.
                    Please select an option from\
                     the menu below.
                    """);
            System.out.println("1. List all media");
            System.out.println("2. List all members");
            System.out.println("3. Loan item");
            System.out.println("4. Return item");
            System.out.println("5. Place reservation");
            System.out.println("6. Add new book");
            System.out.println("7. Exit\n");
            System.out.print("Enter your choice: ");

            // Get users choice
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        listItems(library);
                        break;
                    case "2":
                        listMembers(library);
                        break;
                    case "3":
                        loanItem(library);
                        break;
                    case "4":
                        returnItem(library);
                        break;
                    case "5":
                        placeReservation(library);
                        break;
                    case "6":
                        addBook(library);
                        break;
                    case "7":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1-7.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("\nExiting Library Management System...");
    }


    private void listItems(Library library) {
        System.out.println("\nMedia Items Catalogue:\n");

        for (MediaItem item : library.listItems()) {
            System.out.println(item);
            System.out.println();
        }

        pause();
    }

    private void listMembers(Library library) {
        System.out.println("\nMembers Catalogue:\n");

        for (Member member : library.listMembers()) {
            System.out.println(member);
            System.out.println();
        }

        pause();
    }

    private void loanItem(Library library) {
        try {
            String mq  = readLine("Search for member by name");
            Member member = selectFromList(
                    library.searchMembers(mq),
                    "Results",
                    this::fmtMember
            );

            String iq  = readLine("Search for item by title/author");
            MediaItem item = selectFromList(
                    library.searchMedia(iq),
                    "Results",
                    this::fmtMedia
            );

            boolean cfm = confirm("You are about to create a loan:", List.of(
                    "Member: " + fmtMember(member),
                    "Item: " + fmtMedia(item)
            ));

            if (!cfm) {
                System.out.println("Cancelled. Returning to menu.\n");
                System.out.println();
                pause();
                return;
            }

            Loan loan = library.loanItem(member.getId(), item.getMediaId());
            System.out.println("Loan created successfully.\n");
            System.out.println("Receipt:");
            System.out.println("  Member: " + member.getName());
            System.out.println("  Item:   " + item.getTitle());
            System.out.println("  Due:    " + loan.getDueDate());
            System.out.println();

            pause();

        } catch (CancelledOperationException ignored) {
            System.out.println("Cancelled. Returning to menu.");
            System.out.println();
            pause();
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage() + ".\nReturning to menu.");
            System.out.println();
            pause();
        }
    }

    private void returnItem(Library library) {
        try {
            String q = readLine("Search for item to return (title/author)");
            MediaItem item = selectFromList(
                    library.searchMedia(q),
                    "Matching items",
                    this::fmtMedia
            );

            boolean cfm = confirm("Confirm item to return:", List.of(
                    "Item: " + fmtMedia(item)
            ));

            if (!cfm) {
                System.out.println("Cancelled. Returning to menu.");
                System.out.println();
                pause();
                return;
            }

            Loan loan = library.returnItem(item.getMediaId());
            System.out.println("Loan returned successfully.\n");
            System.out.println("Receipt:");
            System.out.println("  Item:  " + item.getTitle());
            System.out.println("  Fine:  " + String.format("£%.2f", loan.getFineAccrued() / 100.0));
            System.out.println();

            pause();

        } catch (CancelledOperationException ignored) {
            System.out.println("Cancelled. Returning to menu.");
            System.out.println();
            pause();
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage() + "\nReturning to menu.");
            System.out.println();
            pause();
        }
    }

    private void placeReservation(Library library) {
        try {
            String mq =  readLine("Search for member by name");
            Member member = selectFromList(
                    library.searchMembers(mq),
                    "Results",
                    this::fmtMember
            );

            String iq  = readLine("Search for item by title/author");
            MediaItem item = selectFromList(
                    library.searchMedia(iq),
                    "Results",
                    this::fmtMedia
            );

            boolean cfm = confirm("Confirm reservation:", List.of(
                    "Member: " + fmtMember(member),
                    "Item: " + fmtMedia(item)
            ));

            if (!cfm) {
                System.out.println("Cancelled. Returning to menu.");
                System.out.println();
                pause();
                return;
            }

            Reservation reservation = library.placeReservation(member.getId(), item.getMediaId());
            System.out.println("Reservation placed.\n");
            System.out.println("Receipt:");
            System.out.println("  Member: " + member.getName());
            System.out.println("  Item:   " + item.getTitle());
            System.out.println("  ResID:  " + reservation.getReservationId());
            System.out.println();

            pause();

        } catch (CancelledOperationException ignored) {
            System.out.println("Cancelled. Returning to menu.");
            System.out.println();
            pause();
        }  catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage() + "\nReturning to menu.");
            System.out.println();
            pause();
        }
    }

    private void addBook(Library library) {
        try {
            String title = readLine("Enter Book Title: ");
            String author = readLine("Enter Book Author: ");

            String yearInput = readLine("Enter Book Year of Publication");
            int yearOfPublication;

            try {
                yearOfPublication = Integer.parseInt(yearInput);
            } catch (NumberFormatException e) {
                throw new ValidationException("Year must be a number");
            }

            String categoriesInput = readLine("Enter Book Categories (separated by a comma): ");
            List<String> categories = categoriesInput.isEmpty()
                    ? List.of()
                    : List.of(categoriesInput.split("\\s*,\\s*"));

            boolean cfm = confirm("Add book?", List.of(
                    "Title:       " + title,
                    "Author:      " + author,
                    "Year:        " + yearOfPublication,
                    "Categories:  " + (categories.isEmpty() ? "(none)" : String.join(", ", categories))
            ));

            if (!cfm) {
                System.out.println("Cancelled. Returning to menu.");
                System.out.println();
                pause();
                return;
            }

            Book book = new Book(title, author, yearOfPublication, categories);
            library.addItem(book);
            System.out.println("Book added. Book ID: " + book.getMediaId());
            System.out.println();

            pause();

        } catch (CancelledOperationException ignored) {
            System.out.println("Cancelled. Returning to menu.");
            System.out.println();
            pause();
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage() + "\nReturning to menu.");
            System.out.println();
            pause();
        }
    }

    // ---------------------------------------- Internals ---------------------------------------

    private boolean isCancelled(String s) {
        return s.isBlank() || switch  (s.toLowerCase()) {
            case "cancel", "quit", "q", "exit" -> true; //
            default -> false;
        };
    }

    private String readLine(String prompt) {
        System.out.print(prompt + " (Press Enter to cancel): ");
        String input = scanner.nextLine().trim();

        if (isCancelled(input)) {
            throw new CancelledOperationException();
        }
        return input;
    }

    private <T> T selectFromList(
            List<T> options,
            String heading,
            Function<T, String> displayFormatter
    ) {
        if (options == null || options.isEmpty()) {
            System.out.println("No results.\n");
            throw new CancelledOperationException();
        }

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println(heading);
        System.out.println();
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, displayFormatter.apply(options.get(i)));
        }
        System.out.println("----------------------------------------");

        while (true) {
            String input = readLine("Select a number");
            if(input.isBlank()) {
                throw new CancelledOperationException();
            }

            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= options.size()) {
                    return options.get(index - 1);
                } else {
                    System.out.println("Error: Please enter a number from the list.\n");
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Error: Please enter a number from the list.\n");
            }
        }
    }

    private boolean confirm(String title, List<String> lines) {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println(title);
        System.out.println();
        for (String line : lines) {
            System.out.println("  " + line);
        }
        System.out.println("----------------------------------------");
        System.out.println();
        String answer = readLine("Confirm? (Y/n,)").toLowerCase();
        System.out.println();
        return answer.equals("y") || answer.equals("yes");
    }

    private static String shortId(UUID id) {
        String s = id.toString();
        return s.substring(0, 8);
    }

    private String fmtMedia(MediaItem m) {
        String title = (m.getTitle() == null || m.getTitle().isBlank()) ? "(untitled)" : m.getTitle();
        String author = (m instanceof Book b && b.getAuthor() != null && !b.getAuthor().isBlank())
                ? " — " + b.getAuthor()
                : "";
        String status = (m.getStatus() == null) ? "" : " [" + m.getStatus() + "]";
        return title + author + " (id:" + shortId(m.getMediaId()) + ")" + status;
    }

    private String fmtMember(Member member) {
        String name = member.getName() == null ? "(unnamed)" : member.getName();
        String email = member.getEmail() == null || member.getEmail().isBlank() ? "" : " <" + member.getEmail() + ">";
        return name + email + " (id:" + shortId(member.getId()) + ")";
    }

    private void pause() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}
