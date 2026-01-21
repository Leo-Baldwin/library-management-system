package domain.policy;

import domain.model.MediaItem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Fine policy that allows different daily overdue rates by media type.
 */
public class MediaTypeFinePolicy implements FinePolicy {

    private final int defaultPencePerDay;
    private final Map<Class<? extends MediaItem>, Integer> pencePerDayByType;

    /**
     * Creates a policy with per-media-type rates and a default fallback.
     *
     * @param defaultPencePerDay default overdue fine per day in pence
     * @param pencePerDayByType map of media type to fine per day in pence
     */
    public MediaTypeFinePolicy(int defaultPencePerDay,
                               Map<Class<? extends MediaItem>, Integer> pencePerDayByType) {
        if (defaultPencePerDay <= 0) {
            throw new IllegalArgumentException("defaultPencePerDay must be positive");
        }
        this.defaultPencePerDay = defaultPencePerDay;
        this.pencePerDayByType = new HashMap<>();
        if (pencePerDayByType != null) {
            for (Map.Entry<Class<? extends MediaItem>, Integer> entry : pencePerDayByType.entrySet()) {
                Class<? extends MediaItem> type = entry.getKey();
                Integer pence = entry.getValue();
                if (type == null) {
                    throw new IllegalArgumentException("media type cannot be null");
                }
                if (pence == null || pence <= 0) {
                    throw new IllegalArgumentException("pencePerDay must be positive");
                }
                this.pencePerDayByType.put(type, pence);
            }
        }
    }

    /** @return the default fine rate in pence per day */
    public int getDefaultPencePerDay() {
        return defaultPencePerDay;
    }

    /** @return an unmodifiable view of configured per-type fine rates */
    public Map<Class<? extends MediaItem>, Integer> getPencePerDayByType() {
        return Collections.unmodifiableMap(pencePerDayByType);
    }

    @Override
    public int calculateFine(MediaItem mediaItem, LocalDate dueDate, LocalDate returnDate) {
        if (mediaItem == null) {
            throw new IllegalArgumentException("mediaItem cannot be null");
        }
        long daysLate = Math.max(0, ChronoUnit.DAYS.between(dueDate, returnDate));
        long total = daysLate * resolvePencePerDay(mediaItem);
        return (int) total;
    }

    private int resolvePencePerDay(MediaItem mediaItem) {
        Integer configured = pencePerDayByType.get(mediaItem.getClass());
        return (configured == null) ? defaultPencePerDay : configured;
    }
}
