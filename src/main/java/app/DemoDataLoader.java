package app;

import infrastructure.csv.CsvFactory;
import domain.service.Library;
import infrastructure.csv.BookFactory;
import infrastructure.csv.DvdFactory;
import infrastructure.csv.MagazineFactory;
import infrastructure.csv.MemberFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class responsible for loading initial demo data into a {@link Library} from
 * CSV files on initialisation.
 */
public class DemoDataLoader {

    private DemoDataLoader() {
        // Private constructor to prevent instantiation of utility class
    }


    public static void loadDemoData(Library library) {
        loadCsv("data/members.csv",   new MemberFactory(),   library::addMember);
        loadCsv("data/books.csv",     new BookFactory(),     library::addItem);
        loadCsv("data/dvds.csv",      new DvdFactory(),      library::addItem);
        loadCsv("data/magazines.csv", new MagazineFactory(), library::addItem);
    }

    private static <T> void loadCsv(String classpath,
                                    CsvFactory<T> factory,
                                    Consumer<T> consumer) {
        for (String[] row : readCsv(classpath)) {
            if (row.length == 0) continue;
            consumer.accept(factory.fromRow(row));
        }
    }

    // Reads from src/resources/data
    private static List<String[]> readCsv(String classpath) {
        List<String[]> rows = new ArrayList<>();
        try {
            InputStream in = App.class.getClassLoader().getResourceAsStream(classpath);
            if (in == null) {
                System.err.println("CSV file not found on classpath: " + classpath);
                return rows;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line; boolean header = true;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isBlank()) continue;
                    if (header) { header = false; continue; }
                    rows.add(line.split(",", -1));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV " + classpath + ": " + e.getMessage());
        }
        return rows;
    }
}
