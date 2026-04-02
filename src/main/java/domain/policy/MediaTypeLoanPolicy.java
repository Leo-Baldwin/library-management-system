package domain.policy;

import domain.model.MediaItem;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Loan policy that allows different loan periods by media type.
 */
public class MediaTypeLoanPolicy implements LoanPolicy {

    private final int defaultLoanDays;
    private final Map<Class<? extends MediaItem>, Integer> loanDaysByType;

    /**
     * Creates a policy with per-media-type loan periods and a default fallback.
     *
     * @param defaultLoanDays default loan period in days
     * @param loanDaysByType map of media type to loan days
     */
    public MediaTypeLoanPolicy(int defaultLoanDays,
                               Map<Class<? extends MediaItem>, Integer> loanDaysByType) {
        if (defaultLoanDays <= 0) {
            throw new IllegalArgumentException("defaultLoanDays must be positive");
        }
        this.defaultLoanDays = defaultLoanDays;
        this.loanDaysByType = new HashMap<>();
        if (loanDaysByType != null) {
            for (Map.Entry<Class<? extends MediaItem>, Integer> entry : loanDaysByType.entrySet()) {
                Class<? extends MediaItem> type = entry.getKey();
                Integer days = entry.getValue();
                if (type == null) {
                    throw new IllegalArgumentException("media type cannot be null");
                }
                if (days == null || days <= 0) {
                    throw new IllegalArgumentException("loan days must be positive");
                }
                this.loanDaysByType.put(type, days);
            }
        }
    }

    /** @return the default loan period in days */
    public int getDefaultLoanDays() {
        return defaultLoanDays;
    }

    /** @return an unmodifiable view of configured per-type loan days */
    public Map<Class<? extends MediaItem>, Integer> getLoanDaysByType() {
        return Collections.unmodifiableMap(loanDaysByType);
    }

    @Override
    public LocalDate calculateDueDate(MediaItem mediaItem, LocalDate loanDate) {
        if (mediaItem == null) {
            throw new IllegalArgumentException("mediaItem cannot be null");
        }
        if (loanDate == null) {
            throw new IllegalArgumentException("loanDate cannot be null");
        }
        int loanDays = resolveLoanDays(mediaItem);
        return loanDate.plusDays(loanDays);
    }

    private int resolveLoanDays(MediaItem mediaItem) {
        Integer configured = loanDaysByType.get(mediaItem.getClass());
        return (configured == null) ? defaultLoanDays : configured;
    }
}
