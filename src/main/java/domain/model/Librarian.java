package domain.model;

import domain.service.Library;

import java.util.UUID;

/**
 * Represents a library staff member who can manage inventory and assist members within the system.
 * <p>
 *     Extends the abstract {@link Person} class by adding a unique staff
 *     identifier. Librarians perform administrative operations such as adding
 *     and removing media items, processing loans, and managing reservations via
 *     the {@link Library} interface.
 * </p>
 */
public class Librarian extends Person {

    /** Unique staff identifier for this librarian. */
    private final UUID staffNum;

    /** Constructs a new Librarian with a generated staff number.
     *
     * @param name the librarian's full name
     * @param email the librarian's contact email
     */
    public Librarian(String name, String email) {
        super(name, email);
        this.staffNum = UUID.randomUUID(); // Generates a random unique identifier number
    }

    /**
     * Returns the librarian's unique staff identifier.
     *
     * @return UUID representing this librarian's staff number
     */
    public UUID getStaffNum() {
        return staffNum;
    }

    /**
     * Returns a human-readable string representing this librarian.
     *
     * @return formatted string with name, email, and staff number
     */
    @Override
    public String toString() {
        return "Name: " + getName() + ", \n" +
                "Email: " + getEmail() + ", \n" +
                "Staff Number: " + staffNum.toString();
    }
}
