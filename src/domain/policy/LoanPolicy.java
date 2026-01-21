package domain.policy;

import domain.model.MediaItem;

import java.time.LocalDate;

/**
 * Interface for calculating a loans due date.
 */
public interface LoanPolicy {

    /**
     * Calculates a due date for a loan that begins on {@code loanDate}.
     *
     * @param mediaItem the media item being loaned
     * @param loanDate the start date of the loan
     * @return the calculated due date of the loan
     */
    LocalDate calculateDueDate(MediaItem mediaItem, LocalDate loanDate);
}
