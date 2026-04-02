package domain.model;

import java.util.UUID;

/**
 * Abstract superclass that represents any item that can exist within the Library's
 * collection (e.g., Books, DVD's, Magazines).
 * <p>
 *     Encapsulates identity, availability status, and basic behaviours shared by all
 *     media types. Concrete subclasses such as {@link Book}, {@link Dvd}, and {@link Magazine}
 *     provide additional attributes specific to each media item.
 * </p>
 */
public abstract class MediaItem {

    /** Unique identifier for this media item. */
    private final UUID mediaId;

    /** Current availability status of the item. */
    private AvailabilityStatus status;

    /** Constructs a new MediaItem with a generated unique identifier. */
    protected MediaItem() {
        this.mediaId = UUID.randomUUID(); // Generates a random unique identifier number
        this.status = AvailabilityStatus.AVAILABLE;
    }

    /** @return the UUID representing this media item */
    public UUID getMediaId() {
        return mediaId;
    }

    /** @return the current {@link AvailabilityStatus} of this item */
    public AvailabilityStatus getStatus() {
        return status;
    }

    /** @return the books title */
    public abstract String getTitle();

    /**
     * Sets a new availability status for the media item.
     *
     * @param status the new {@link AvailabilityStatus} to assign
     * @throws IllegalArgumentException if {@code status} is null
     */
    public void setStatus(AvailabilityStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    /**
     * Indicates whether the media item is currently available to be borrowed.
     *
     * @return true if the status is AVAILABLE; false if not
     */
    public boolean isAvailable() {
        return status == AvailabilityStatus.AVAILABLE;
    }

    /** @return a formatted string containing the id number and current status of this item */
    @Override
    public String toString() {
        return getClass().getSimpleName() + ", \n" +
                "MediaId: " + mediaId + ", \n" +
                "Status: " + status;
    }
}
