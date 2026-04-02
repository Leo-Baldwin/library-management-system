package domain.model;

import java.util.ArrayList;
import java.util.List;

/** Concrete media type representing a book in the Library's collection.
 * <p>
 *     Extends {@link MediaItem} and adds entity specific attributes such as
 *     title, authors, year of publication, and categories.
 * </p>
 */
public class Book extends MediaItem {

    /** Title of the book. */
    private String title;

    /** Full name of the author. */
    private String author;

    /** Year of publication (e.g., 2004). */
    private int yearOfPublish;

    /** Category labels (e.g. 'Fiction', 'History'). */
    private final List<String> categories = new ArrayList<>();

    /**
     * Constructs a Book with full metadata.
     *
     * @param title book title
     * @param author name of the author
     * @param yearOfPublish year of publication
     * @param categories list of category labels
     */
    public Book(String title, String author, int yearOfPublish,  List<String> categories) {
        super();
        setTitle(title);
        setAuthor(author);
        setYearOfPublish(yearOfPublish);
        setCategories(categories);
    }

    /** @return the books title */
    public String getTitle() {
        return title;
    }

    /** @param title new title; cannot be null/blank */
    public void setTitle(String title) {
        if ((title == null) || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        this.title = title;
    }

    /** @return the authors name */
    public String getAuthor() {
        return author;
    }

    /** @param author new author name; cannot be null/blank */
    public void setAuthor(String author) {
        if ((author == null) || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or blank");
        }
        this.author = author;
    }

    /** @return the year of publication (0 if unknown) */
    public int getYearOfPublish() {
        return yearOfPublish;
    }

    /** @param yearOfPublish non-negative publication year (0 if unknown) */
    public void setYearOfPublish(int yearOfPublish) {
        if  (yearOfPublish < 0) {
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

    /** @return a formatted string representing the book, including title and author */
    @Override
    public String toString() {
        return "Book: \n" +
                "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Year of publish: " + yearOfPublish + "\n" +
                "Categories: " + getCategories() + "\n" +
                "Status: " + getStatus();
    }
}
