package infrastructure.csv;

/**
 * A factory interface for creating objects from CSV data.
 * <p>
 *     Concrete implementations of this interface define the logic for
 *     mapping a CSV row to a domain object. This interface is typically
 *     used during application startup to load initial data from CSV files.
 * </p>
 *
 * @param <T> the type of object being created by the factory interface
 */
public interface CsvFactory<T> {

    /**
     * Creates an instance of {@code T} from a row of CSV data.
     *
     * @param row a {@code String[]} representing one CSV record
     * @return the constructed object
     */
    T fromRow(String[] row);
}