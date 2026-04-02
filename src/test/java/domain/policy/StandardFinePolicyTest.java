package domain.policy;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StandardFinePolicyTest {

    private final LocalDate dueDate = LocalDate.of(2025, 11, 15);

    @Test
    void returnBeforeDueDateHasNoFine() {
        StandardFinePolicy policy = new StandardFinePolicy(50);

        assertEquals(0, policy.calculateFine(null, dueDate, LocalDate.of(2025, 11, 13)));
    }

    @Test
    void returnOnDueDateHasNoFine() {
        StandardFinePolicy policy = new StandardFinePolicy(50);

        assertEquals(0, policy.calculateFine(null, dueDate, dueDate));
    }

    @Test
    void returnOneDayLateChargesOneDayFine() {
        StandardFinePolicy policy = new StandardFinePolicy(50);

        assertEquals(50, policy.calculateFine(null, dueDate, LocalDate.of(2025, 11, 16)));
    }

    @Test
    void returnFiveDaysLateChargesFiveDaysFine() {
        StandardFinePolicy policy = new StandardFinePolicy(50);

        assertEquals(250, policy.calculateFine(null, dueDate, LocalDate.of(2025, 11, 20)));
    }

    @Test
    void negativePencePerDayThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new StandardFinePolicy(-10));
    }
}
