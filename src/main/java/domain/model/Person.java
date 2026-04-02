package domain.model;

import common.ValidationException;

import java.util.UUID;

/**
 * Abstract superclass that represents a person within the library system.
 * <p>
 *     Provides the shared identity and contact attributes for all subclasses,
 *     such as {@link Member} and {@link Librarian}.
 * </p>
 */
public abstract class Person {

    /** Unique identifier for this person. */
    private final UUID id;

    /** Person's full name. */
    private String name;

    /** Contact email address for this person. */
    private String email;

    /**
     * Constructs a new Person instance with a generated unique ID.
     *
     * @param name the person's full name
     * @param email the person's email address; must contain '@'
     * @throws IllegalArgumentException if the email is invalid
     */
    protected Person(String name, String email) {
        this.id = UUID.randomUUID(); // Generates a random unique identifier number
        setName(name);
        setEmail(email); // Validation handled in setter
    }

    /** @return the UUID representing this person */
    public UUID getId() {
        return id;
    }

    /** @return the persons full name */
    public String getName() {
        return name;
    }

    /** @param name new full name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the persons email */
    public String getEmail() {
        return email;
    }

    /**
     * Validates and sets a new email address for this person.
     * <p>
     *     Must not be empty and contain at least one '@' symbol to be valid email.
     * </p>
     *
     * @param email the new email address
     * @throws IllegalArgumentException if the email is invalid
     */
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Invalid email address");
        }
        this.email = email;
    }

    /** @return a formatted string representing the person, with name and email */
    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Email: " + email;
    }
}
