package domain.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a members request to loan a currently unavailable {@link MediaItem} once it has been returned.
 * <p>
 *     A reservation begins as {@link ReservationStatus#ACTIVE} by default when created and transitions to
 *     {@link ReservationStatus#FULFILLED} when the item becomes available and is allocated to member with
 *     reservation, or {@link ReservationStatus#CANCELLED} if the member or librarian cancels it.
 * </p>
 */
public class Reservation {

    /** Unique identifier for this reservation. */
    private final UUID reservationId;

    /** Identifier of the member reserving the item. */
    private final UUID memberId;

    /** Identifier of the media item being reserved. */
    private final UUID mediaId;

    /** The date the reservation was created. */
    private final LocalDate createdDate;

    /** The current status of the reservation. */
    private ReservationStatus status;

    /**
     * Creates a new ACTIVE reservation with a generated unique identifier.
     *
     * @param memberId the reserving members UUID
     * @param mediaId the UUID of the media item being reserved
     * @param createdDate the creation date of the reservation
     */
    public Reservation(UUID memberId, UUID mediaId, LocalDate createdDate) {
        // Throws illegal argument exception if any of the arguments given in parameter are null
        if (memberId == null || mediaId == null || createdDate == null){
            throw new IllegalArgumentException("MemberId, mediaId, and created date cannot be null");
        }
        this.reservationId = UUID.randomUUID(); // Generates a random UUID number
        this.memberId = memberId;
        this.mediaId = mediaId;
        this.createdDate = createdDate;
        this.status = ReservationStatus.ACTIVE; // Sets default state to ACTIVE
    }

    /** Marks the reservation as fulfilled. */
    public void fulfil() {
        this.status = ReservationStatus.FULFILLED;
    }

    /** Marks the reservation as cancelled. */
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    // Getters

    /** @return the unique reservation ID */
    public UUID getReservationId() {
        return reservationId;
    }

    /** @return the reserving members ID */
    public UUID getMemberId() {
        return memberId;
    }

    /** @return the ID of the media item being reserved */
    public UUID getMediaId() {
        return mediaId;
    }

    /** @return the creation date of the reservation */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /** @return the current state of the reservation */
    public ReservationStatus getStatus() {
        return status;
    }

    /** @return a formatted string representing the reservation, including relevant ID's, the creation
     * date, and current status
     * */
    @Override
    public String toString() {
        return "Reservation: \n" +
                "ReservationId: " + reservationId + "\n" +
                "MemberId: " + memberId + "\n" +
                "MediaId: " + mediaId + "\n" +
                "Created Date: " + createdDate + "\n" +
                "Status: " + status;
    }
}
