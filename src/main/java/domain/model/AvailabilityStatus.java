package domain.model;

/**
 * Represents the availability of a MediaItem within the library system.
 */
public enum AvailabilityStatus {
    AVAILABLE,  // Item can be loaned or reserved
    ON_LOAN,    // Item is currently loaned to a member
    RESERVED    // Item is being held for a reservation (not available to others)
}
