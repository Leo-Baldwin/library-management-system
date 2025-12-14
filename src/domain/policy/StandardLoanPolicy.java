package domain.policy;

import java.time.LocalDate;

/**
 * A standard implementation of the {@link LoanPolicy} interface that calculates a due date by
 * adding a fixed number of days to the loan date.
 */
public class StandardLoanPolicy implements LoanPolicy {

    /** Number of days to add to the loan starting date to get the due date. */
    private final int loanDays;

    /**
     * Creates a policy with the specified loan period
     *
     * @param loanDays number of days for the loan period
     */
    public StandardLoanPolicy(int loanDays) {
        this.loanDays = loanDays;
    }

    /** @return the loan period in days
     */
    public int getLoanDays() {
        return loanDays;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate calculateDueDate(LocalDate loanDate) {
        if (loanDate == null) {
            throw new IllegalArgumentException("loanDate cannot be null");
        }
        return loanDate.plusDays(loanDays);
    }
}
