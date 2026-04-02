package infrastructure.json;

import domain.model.Loan;
import domain.model.MediaItem;
import domain.model.Member;
import domain.model.Reservation;

import java.util.List;

/**
 * Data transfer object representing the full serializable state of the library.
 */
public class LibraryState {
    private final List<MediaItem> items;
    private final List<Member> members;
    private final List<Loan> loans;
    private final List<Reservation> reservations;

    public LibraryState(List<MediaItem> items, List<Member> members,
                        List<Loan> loans, List<Reservation> reservations) {
        this.items = items;
        this.members = members;
        this.loans = loans;
        this.reservations = reservations;
    }

    public List<MediaItem> getItems() { return items; }
    public List<Member> getMembers() { return members; }
    public List<Loan> getLoans() { return loans; }
    public List<Reservation> getReservations() { return reservations; }
}
