package domain.model;

/**
 * Represents a library member who can borrow and reserve media items.
 * <p>
 *     Extends the abstract {@link Person} class by adding member specific
 *     attributes and methods. Each member is uniquely identified within the
 *     library system by their inherited {@code id}.
 * </p>
 */
public class Member extends Person {

    /** Indicates whether the person is currently an active member. */
    private boolean activeMember;

    /**
     * Constructs a new active Member with name and email inherited from superclass.
     *
     * @param name the member's full name
     * @param email the member's email address
     */
    public Member(String name, String email) {
        super(name, email);
        this.activeMember = true; // default to active
    }

    /** @return {@code true} if the member is active */
    public boolean isActiveMember() {
        return activeMember;
    }

    /** @param activeMember {@code true} to activate membership, {@code false} to deactivate */
    public void setActiveMember(boolean activeMember) {
        this.activeMember = activeMember;
    }

    /** @return formatted string with name, email, and activity status */
    @Override
    public String toString() {
        return "Name: " + getName() + ", \n" +
                "Email: " + getEmail() + ", \n" +
                "Active: " + activeMember;
    }
}
