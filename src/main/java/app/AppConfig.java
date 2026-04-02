package app;

import domain.model.Book;
import domain.model.Dvd;
import domain.model.Magazine;
import domain.model.MediaItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Loads application configuration from {@code application.properties} on the classpath.
 */
public class AppConfig {

    private final Properties props;

    public AppConfig() {
        props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public int getServerPort() {
        return Integer.parseInt(props.getProperty("server.port", "8080"));
    }

    public int getDefaultLoanDays() {
        return Integer.parseInt(props.getProperty("loan.default.days", "14"));
    }

    public Map<Class<? extends MediaItem>, Integer> getLoanDaysByType() {
        Map<Class<? extends MediaItem>, Integer> map = new HashMap<>();
        putIfPresent(map, Book.class, "loan.days.Book");
        putIfPresent(map, Dvd.class, "loan.days.Dvd");
        putIfPresent(map, Magazine.class, "loan.days.Magazine");
        return map;
    }

    public int getDefaultFinePencePerDay() {
        return Integer.parseInt(props.getProperty("fine.default.pencePerDay", "50"));
    }

    public Map<Class<? extends MediaItem>, Integer> getFineRatesByType() {
        Map<Class<? extends MediaItem>, Integer> map = new HashMap<>();
        putIfPresent(map, Book.class, "fine.pencePerDay.Book");
        putIfPresent(map, Dvd.class, "fine.pencePerDay.Dvd");
        putIfPresent(map, Magazine.class, "fine.pencePerDay.Magazine");
        return map;
    }

    private void putIfPresent(Map<Class<? extends MediaItem>, Integer> map,
                              Class<? extends MediaItem> type, String key) {
        String value = props.getProperty(key);
        if (value != null) {
            map.put(type, Integer.parseInt(value));
        }
    }
}
