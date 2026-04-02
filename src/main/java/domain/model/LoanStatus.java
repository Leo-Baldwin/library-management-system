package domain.model;

/**
 * Represents the state of a Loan within the library system.
 */
public enum LoanStatus {
    OUTSTANDING,    // Loan is currently active
    RETURNED        // MediaItem has been returned and Loan has been closed
}
