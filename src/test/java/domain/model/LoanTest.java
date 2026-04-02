package domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    private Loan createLoan(LocalDate loanDate, LocalDate dueDate) {
        return new Loan(UUID.randomUUID(), UUID.randomUUID(), loanDate, dueDate);
    }

    @Test
    void markReturnedSetsStatusAndDate() {
        Loan loan = createLoan(LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 25));
        LocalDate returnDate = LocalDate.of(2025, 11, 20);

        loan.markReturned(returnDate);

        assertEquals(LoanStatus.RETURNED, loan.getStatus());
        assertEquals(returnDate, loan.getReturnDate());
    }

    @Test
    void markReturnedCannotBeCalledTwice() {
        Loan loan = createLoan(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 10));
        loan.markReturned(LocalDate.of(2025, 11, 20));

        assertThrows(IllegalStateException.class, () -> loan.markReturned(LocalDate.of(2025, 11, 21)));
    }

    @Test
    void isOverdueFalseBeforeDueDate() {
        Loan loan = createLoan(LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));

        assertFalse(loan.isOverdue(LocalDate.of(2025, 11, 15)));
    }

    @Test
    void isOverdueTrueAfterDueDate() {
        Loan loan = createLoan(LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));

        assertTrue(loan.isOverdue(LocalDate.of(2025, 11, 25)));
    }

    @Test
    void isOverdueRejectsNullCurrentDate() {
        Loan loan = createLoan(LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));

        assertThrows(IllegalArgumentException.class, () -> loan.isOverdue(null));
    }

    @Test
    void setFineAccruedRejectsNegative() {
        Loan loan = createLoan(LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));

        assertThrows(IllegalArgumentException.class, () -> loan.setFineAccrued(-1));
    }

    @Test
    void setFineAccruedStoresPositive() {
        Loan loan = createLoan(LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 20));

        loan.setFineAccrued(250);

        assertEquals(250, loan.getFineAccrued());
    }
}
