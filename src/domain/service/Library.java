package domain.service;

import common.ValidationException;
import domain.model.*;
import domain.policy.FinePolicy;
import domain.policy.LoanPolicy;

import java.time.LocalDate;
import java.util.*;

/**
 * Aggregate root class that coordinates all the behaviour for domain entities within the
 * library system.
 * <p>
 *     Stores the collections of {@link MediaItem}s, {@link Loan}s, {@link Member}s, and
 *     {@link Reservation}s, and performs behaviours such as adding/removing items,
 *     loaning/returning items, and managing reservations.
 * </p>
 */
public class Library {

    /**
     * All media items by ID.
     */
    private final Map<UUID, MediaItem> items = new HashMap<>();

    /**
     * All loans by ID.
     */
    private final Map<UUID, Loan> loans = new HashMap<>();

    /**
     * All members by ID.
     */
    private final Map<UUID, Member> members = new HashMap<>();

    /**
     * All reservations by ID.
     */
    private final Map<UUID, Deque<Reservation>> reservationsByMediaItem = new HashMap<>();

    /**
     * Policy for calculating due dates.
     */
    private final LoanPolicy loanPolicy;

    /**
     * Policy for calculating fines.
     */
    private final FinePolicy finePolicy;

    /**
     * Constructs a Library aggregate with configured loan and fine policies.
     *
     * @param loanPolicy policy for calculating due dates; must not be null
     * @param finePolicy policy for calculating fines; must not be null
     */
    public Library(LoanPolicy loanPolicy, FinePolicy finePolicy) {
        if (loanPolicy == null || finePolicy == null) {
            throw new ValidationException("Policies cannot be null");
        }
        this.loanPolicy = loanPolicy;
        this.finePolicy = finePolicy;
    }

    // ---------------------------------------- Items ----------------------------------------

    /**
     * Adds a media item to the items Map, with its UUID as the key.
     *
     * @param item a non null {@link MediaItem}
     */
    public void addItem(MediaItem item) {
        if (item == null) {
            throw new ValidationException("Item cannot be null");
        }
        items.put(item.getMediaId(), item);
    }

    /**
     * Removes a media item from the items Map.
     *
     * @param mediaId the ID of the item to remove
     */
    public void removeItem(UUID mediaId) {
        // Retrieves the item from items Map by its ID
        MediaItem item = items.get(mediaId);

        if (!item.isAvailable()) {
            throw new ValidationException("Cannot remove: item is not available");
        } else if (hasActiveReservation(mediaId)) {
            throw new ValidationException("Cannot remove: item has active reservation");
        }
        items.remove(mediaId);
    }

    // ---------------------------------------- Members --------------------------------------

    /**
     * Adds a member to the members Map, with its UUID as the key
     *
     * @param member a non null {@link Member}
     */
    public void addMember(Member member) {
        if (member == null) {
            throw new ValidationException("Member cannot be null");
        }
        members.put(member.getId(), member);
    }

    /**
     * Removes a member from the members Map.
     *
     * @param memberId the ID of the member to remove
     */
    public void removeMember(UUID memberId) {
        // Retrieves the member from members Map by their ID
        Member member = members.get(memberId);

        if (memberHasOverdueLoans(memberId)) {
            throw new ValidationException("Cannot remove: member has overdue loans");
        }
        members.remove(memberId);
    }

    // ---------------------------------------- Loans ----------------------------------------

    /**
     * Create a new loan on an available item for member.
     * <p>
     * Invariants:
     *     <ul>
     *         <li>Member must be active.</li>
     *         <li>Member must not have overdue loans.</li>
     *         <li>Item must be AVAILABLE.</li>
     *     </ul>
     * </p>
     *
     * @param memberId the ID of the member borrowing item
     * @param mediaId  the ID of the item being borrowed
     * @return the created {@link Loan}
     */
    public Loan loanItem(UUID memberId, UUID mediaId) {
        Member member = members.get(memberId);
        MediaItem item = items.get(mediaId);

        // Checks for invariant complicity
        if (!member.isActiveMember()) {
            throw new ValidationException("Cannot loan item while inactive member");
        } else if (memberHasOverdueLoans(memberId)) {
            throw new ValidationException("Cannot loan item with overdue loans");
        } else if (!item.isAvailable()) {
            throw new ValidationException("Item is not currently available");
        }

        // Gets current date and calculates the loans due date
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanPolicy.calculateDueDate(loanDate);

        // Creates new loan object and add to loans Map
        Loan loan = new Loan(member.getId(), item.getMediaId(), loanDate, dueDate);
        loans.put(loan.getLoanId(), loan);

        // Changes item state
        item.setStatus(AvailabilityStatus.ON_LOAN);

        return loan;
    }

    /**
     * Return an item and close a loan for member.
     *
     * @param mediaId the ID of the item being returned
     * @return the returned {@link Loan}
     */
    public Loan returnItem(UUID mediaId) {
        MediaItem item = items.get(mediaId);
        Loan loan = findOpenLoanByMediaId(mediaId);

        // Gets current date and calculates any fine accrued
        LocalDate returnDate = LocalDate.now();
        int fine = finePolicy.calculateFine(loan.getDueDate(), returnDate);

        // Records fine amount
        loan.setFineAccrued(fine);

        // Changes loan status to RETURNED and record return date
        loan.markReturned(returnDate);

        // Updates item status to RESERVED if it has a reservation; else AVAILABLE
        if (hasActiveReservation(mediaId)) {
            item.setStatus(AvailabilityStatus.RESERVED);
        } else {
            item.setStatus(AvailabilityStatus.AVAILABLE);
        }
        return loan;
    }

    // ---------------------------------------- Reservations ---------------------------------

    /**
     * Creates a reservation on a media item for member.
     *
     * @param memberId the ID of the member placing reservation
     * @param mediaId the ID of the item being reserved
     * @return the successfully placed {@link Reservation}
     */
    public Reservation placeReservation(UUID memberId, UUID mediaId) {
        Member member = members.get(memberId);

        if (member == null) {
            throw new ValidationException("Member not found.");
        }

        if (!member.isActiveMember()) {
            throw new ValidationException("Inactive members cannot reserve items.");
        }

        // Gets queue of reservations for given mediaId
        Deque<Reservation> reservations = reservationsByMediaItem.computeIfAbsent(mediaId,
                id -> new ArrayDeque<>());
        Reservation r = new Reservation(memberId, mediaId, LocalDate.now());
        reservations.addLast(r);
        return r;
    }


    public boolean fulfillReservation(UUID mediaId) {
        MediaItem item = items.get(mediaId);
        if (item == null) throw new ValidationException("Item not found.");

        if(fulfillNextReservation(mediaId)) {
            item.setStatus(AvailabilityStatus.RESERVED);
            return true;
        }
        return false;
    }

    // ---------------------------------------- Lookups and Listings -------------------------

    public List<MediaItem> listItems() {
        return new ArrayList<>(items.values());
    }

    public List<Member> listMembers() {
        return new ArrayList<>(members.values());
    }

    public List<MediaItem> searchMedia(String keyword) {
        if (keyword == null) keyword = "";
        String q = keyword.toLowerCase();
        List<MediaItem> results = new ArrayList<>();
        for (MediaItem item : items.values()) {
            String title = item.getTitle() == null ? "" : item.getTitle().toLowerCase();

            String author = "";
            if (item instanceof Book book) {
                author = (book.getAuthor() == null) ? "" : book.getAuthor().toLowerCase();
            }

            if (title.contains(q) || author.contains(q)) {
                results.add(item);
            }
        }
        results.sort(Comparator.comparing(
                m -> m.getTitle() == null ? "" : m.getTitle(),
                String.CASE_INSENSITIVE_ORDER
        ));
        return results;
    }

    public List<Member> searchMembers(String keyword) {
        if (keyword == null) keyword = "";
        String q = keyword.toLowerCase();
        List<Member> results = new ArrayList<>();
        for (Member member : members.values()) {
            String name = member.getName() == null ? "" : member.getName().toLowerCase();

            if (name.contains(q)) {
                results.add(member);
            }
        }
        results.sort(Comparator.comparing(
                m -> m.getName() == null ? "" : m.getName(),
                String.CASE_INSENSITIVE_ORDER)
        );
        return results;
    }

    // ---------------------------------------- Internals ------------------------------------

    /**
     * Checks if an active reservation currently exists on a given media item.
     *
     * @param mediaId the ID of the item being checked for an active reservation
     * @return {@code true} if an active reservation exists, {@code false} if no active reservation
     */
    private boolean hasActiveReservation(UUID mediaId) {
        Deque<Reservation> reservations = reservationsByMediaItem.get(mediaId);
        if (reservations == null || reservations.isEmpty()) return false;
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.ACTIVE) return true;
        }
        return false;
    }

    /**
     * Looks for and returns any outstanding loans for a given media item.
     *
     * @param mediaId the ID of the item being checked for outstanding loans
     * @return the outstanding loan if it exists, else returns a ValidationException message
     */
    private Loan findOpenLoanByMediaId(UUID mediaId) {
        for (Loan loan : loans.values()) {
            if (loan.getMediaId().equals(mediaId) && loan.getStatus() == LoanStatus.OUTSTANDING) {
                return loan;
            }
        }
        throw new ValidationException("No open loan found for mediaId: " + mediaId);
    }

    private boolean memberHasOverdueLoans (UUID memberId) {
        LocalDate date = LocalDate.now();

        for  (Loan loan : loans.values()) {
            if (loan.getMemberId().equals(memberId)
                    && loan.getStatus() == LoanStatus.OUTSTANDING
                    && loan.getDueDate().isBefore(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds and fulfills the next reservation on a media item if one exists.
     *
     * @param mediaId the ID of the item being checked for the next active reservation
     * @return {@code true} if active reservation was found and fulfilled, else returns {@code false}
     */
    private boolean fulfillNextReservation(UUID mediaId) {
        Deque<Reservation> reservations = reservationsByMediaItem.get(mediaId);

        if (reservations == null) return false;

        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.ACTIVE) {
                reservation.fulfil();
                return true;
            }
        }
        return false;
    }
}
