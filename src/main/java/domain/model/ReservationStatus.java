package domain.model;

/**
 * Represents the lifecycle state of a Reservation within the library system.
 */
public enum ReservationStatus {
    ACTIVE,     // Reservation is active and awaiting availability
    FULFILLED,  // Reservation has been fulfilled and is ready or collected
    CANCELLED   // Reservation was cancelled by member or librarian
}
