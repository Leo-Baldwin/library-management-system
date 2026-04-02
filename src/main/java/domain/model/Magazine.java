package domain.model;

import java.util.ArrayList;
import java.util.List;

/** Concrete media type representing a magazine in the Library's collection.
 * <p>
 *     Extends {@link MediaItem} and adds entity specific attributes such as title
 *     publisher, year of publication, and categories.
 * </p>
  */
public class Magazine extends MediaItem {

    /** Title of the magazine. */
    private String title;

    /** Publisher or editor of this issue. */
    private String publisher;

    /** Year of publication of this issue. */
    private int yearOfPublish;

    /** Category labels (e.g. 'Technology', 'Fashion'). */
    private final List<String> categories =  new ArrayList<>();

    /**
     * Constructs a Magazine with full metadata.
     *
     * @param title magazine title
     * @param publisher author or editor name
     * @param yearOfPublish year of publication
     * @param categories list of category labels
     */
    public Magazine(String title, String publisher, int yearOfPublish, List<String> categories) {
        super();
        setTitle(title);
        setPublisher(publisher);
        setYearOfPublish(yearOfPublish);
        setCategories(categories);
    }

    /** @return the magazines title */
    public String getTitle() {
        return title;
    }

    /**  @param title new title; cannot be null/blank */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        this.title = title;
    }

    /** @return the publishers name */
    public String getPublisher() {
        return publisher;
    }

    /** @param publisher new publisher name; cannot be null/blank */
    public void setPublisher(String publisher) {
        if (publisher == null || publisher.isBlank()) {
            throw new IllegalArgumentException("Publisher cannot be null or blank");
        }
        this.publisher = publisher;
    }

    /** @return the year of publication (0 if unknown) */
    public int getYearOfPublish() {
        return yearOfPublish;
    }

    /** @param yearOfPublish non-negative publication year (0 if unknown) */
    public void setYearOfPublish(int yearOfPublish) {
        if (yearOfPublish < 0) {
            throw new IllegalArgumentException("Year of publish cannot be negative");
        }
        this.yearOfPublish = yearOfPublish;
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

    /** @return a formatted string representing the magazine, including title and publisher */
    @Override
    public String toString() {
        return "Magazine: \n" +
                "Title: " + title + "\n" +
                "Publisher: " + publisher + "\n" +
                "Year of publish: " + yearOfPublish + "\n" +
                "Categories: " + getCategories() + "\n" +
                "Status: " + getStatus();
    }
}
