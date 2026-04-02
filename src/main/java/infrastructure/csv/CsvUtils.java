package infrastructure.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing helper methods for the factory classes to create instances of domain classes
 * using data from CSV files.
 */
public final class CsvUtils {

    private CsvUtils() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Parses an {@code int} value from a string taken from a CSV file. Returns a fallback value
     * if the input produces an error during parsing.
     *
     * @param s the string to parse
     * @param fallback backup value to return if parsing fails
     * @return the parsed {@code int}, or {@code fallback} if parsing fails
     */
    public static int parseInt(String s, int fallback) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return fallback; }
    }

    /**
     * Splits a category fields into a list of trimmed strings.
     *
     * @param field the pipe ("|") separated category field (e.g. {@code "Fiction|Adventure|Classic"})
     * @return a list of non-blank category strings
     */
    public static List<String> splitCats(String field) {
        List<String> out = new ArrayList<>();
        if (field == null || field.isBlank()) return out;
        for (String c : field.split("\\|")) {
            String v = c.trim();
            if (!v.isBlank()) out.add(v);
        }
        return out;
    }
}