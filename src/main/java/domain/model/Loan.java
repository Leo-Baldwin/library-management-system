package domain.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a loan record for a single {@link MediaItem} by a {@link Member}.
 * <p>
 *     When an available media item is checked out a loan is created with the
 *     status {@link LoanStatus#OUTSTANDING}. This status remains the same until
 *     the item is returned, upon which the loan transitions to {@link LoanStatus#RETURNED},
 *     and any fine is recorded.
 * </p>
 */
public class Loan {

    /** Unique identifier for this loan record. */
    private final UUID loanId;

    /** Identifier of the member borrowing the item. */
    private final UUID memberId;

    /** Identifier of the media item being borrowed. */
    private final UUID mediaId;

    /** The date the item was checked out. */
    private final LocalDate loanDate;

    /** The date the item is due to be returned. */
    private final LocalDate dueDate;

    /** The date the item was returned, null until returned. */
    private LocalDate returnDate;

    /** The current status of the loan. */
    private LoanStatus status;

    /** Fine accrued in pence; or 0 if no fine. */
    private int fineAccrued;

    /**
     * Creates a new OUTSTANDING loan with a generated unique identifier.
     *
     * @param memberId the borrowing members UUID
     * @param mediaId the UUID of the media item being borrowed
     * @param loanDate the date the loan was created
     * @param dueDate the date the item is due to be returned
     */
    public Loan(UUID memberId, UUID mediaId, LocalDate loanDate, LocalDate dueDate) {
        this.loanId = UUID.randomUUID(); // Generates a random UUID number
        this.memberId = memberId;
        this.mediaId = mediaId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = LoanStatus.OUTSTANDING; // Sets default state to OUTSTANDING
    }

    /**
     * Marks the loan status as returned and records the return date.
     * <p>
     *     Sets {@code status} to {@link LoanStatus#RETURNED} and stores {@link #returnDate}.
     *     Loan cannot be returned twice and the return date cannot be null.
     * </p>
     *
     * @param returnDate the date the item was returned
     */
    public void markReturned(LocalDate returnDate) {
        if (status == LoanStatus.RETURNED) {
            throw new IllegalStateException("Loan is already returned, cannot be returned twice.");
        }

        this.returnDate = returnDate;
        // Marks the loan as returned
        this.status = LoanStatus.RETURNED;
    }

    /**
     * Checks whether overdue relative to the current date.
     *
     * @param currentDate the current date used to compare against the return date
     * @return {@code true} if loan outstanding {@code currentDate} is after {@code dueDate}; otherwise {@code false}
     */
    public boolean isOverdue(LocalDate currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("Current Date cannot be null.");
        }
        // Returns true only if loan is still outstanding and past due date
        return status == LoanStatus.OUTSTANDING && currentDate.isAfter(dueDate);
    }

    /**
     * Sets the fine accrued on the loan in pence.
     *
     * @param pence the fine amount in pence, non-negative
     */
    public void setFineAccrued(int pence) {
        if (pence < 0) {
            throw new IllegalArgumentException("Pence cannot be negative.");
        }
        this.fineAccrued = pence;
    }

    // Getters

    /** @return the unique loan ID */
    public UUID getLoanId() {
        return loanId;
    }

    /** @return the borrowing members ID */
    public UUID getMemberId() {
        return memberId;
    }

    /** @return the ID of the media item being borrowed */
    public UUID getMediaId() {
        return mediaId;
    }

    /** @return the date the loan was issued */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /** @return the date the item is due to be returned */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /** @return the date the item was returned; null if not yet returned */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /** @return the current status of the loan, OUTSTANDING or RETURNED */
    public LoanStatus getStatus() {
        return status;
    }

    /** @return the fine accrued on the loan, 0 if no fine */
    public int getFineAccrued() {
        return fineAccrued;
    }

    /** @return a formatted string representing the loan, including relevant ID's, dates and loan status */
    @Override
    public String toString() {
        return "Loan: \n" +
                "LoanId: " + loanId + "\n" +
                "MemberId: " + memberId + "\n" +
                "MediaId: " + mediaId + "\n" +
                "Start Date: " + loanDate + "\n" +
                "Due Date: " + dueDate + "\n" +
                "Status: " + status;
    }
}
