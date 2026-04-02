package domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete media type representing a DVD in the Library's collection.
 * <p>
 *     Extends {@link MediaItem} and adds entity specific attributes such as
 *     title, directors, age rating, and categories.
 * </p>
 */
public class Dvd extends MediaItem {

    /** Title of the Dvd. */
    private String title;

    /** Year of release. */
    private int yearOfRelease;

    /** Duration of the Dvd in minutes. */
    private int durationMinutes;

    /** Age rating of the Dvd. */
    private String ageRating;

    /** Category labels (e.g. 'Horror', 'Comedy'). */
    private final List<String> categories =  new ArrayList<>();

    /**
     * Constructs a Dvd with full metadata.
     *
     * @param title Dvd title
     * @param yearOfRelease year of release
     * @param durationMinutes runtime in minutes
     * @param ageRating age rating label
     * @param categories list of category labels
     */
    public Dvd(String title, int yearOfRelease, int durationMinutes, String ageRating, List<String> categories) {
        super();
        setTitle(title);
        setYearOfRelease(yearOfRelease);
        setDurationMinutes(durationMinutes);
        setAgeRating(ageRating);
        setCategories(categories);
    }

    /** @return the Dvd's title */
    public String getTitle() {
        return title;
    }

    /** @param title new title; cannot be null/blank */
    public void setTitle(String title) {
        if  (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        this.title = title;
    }

    /** @return the year of release (0 if unknown) */
    public int getYearOfRelease() {
        return yearOfRelease;
    }

    /** @param yearOfRelease non-negative year of release (0 if unknown) */
    public void setYearOfRelease(int yearOfRelease) {
        if (yearOfRelease < 0) {
            throw new IllegalArgumentException("Year of release cannot be negative");
        }
        this.yearOfRelease = yearOfRelease;
    }

    /** @return the runtime in minutes */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /** @param durationMinutes new runtime in minutes; cannot be null/blank */
    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        this.durationMinutes = durationMinutes;
    }

    /** @return the age rating label */
    public String getAgeRating() {
        return ageRating;
    }

    /** @param ageRating new age rating label; cannot be null/blank */
    public void setAgeRating(String ageRating) {
        if (ageRating == null || ageRating.isBlank()) {
            throw new IllegalArgumentException("AgeRating cannot be null or blank");
        }
        this.ageRating = ageRating;
    }

    /** @return a copy of the list of categories */
    public List<String> getCategories() {
        return List.copyOf(categories);
    }

    /** @param categories new list of category labels; cannot be null/empty */
    public void setCategories(List<String> categories) {
        if  (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("Categories cannot be null or empty");
        }
        this.categories.clear();
        this.categories.addAll(categories);
    }

    /** @return a formatted string representing the DVD, including title, duration and age rating */
    @Override
    public String toString() {
        return "DVD: \n" +
                "Title: " + title + " \n" +
                "Duration: " + durationMinutes + " \n" +
                "AgeRating: " + ageRating + " \n" +
                "Categories: " + getCategories() + "\n" +
                "Status: " + getStatus();
    }

}
