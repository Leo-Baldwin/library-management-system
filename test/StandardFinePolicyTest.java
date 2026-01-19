import domain.policy.StandardFinePolicy;

import java.time.LocalDate;

/**
 * Unit tests for StandardFinePolicy class.
 */
public class StandardFinePolicyTest {

    public static void main(String[] args) {
        StandardFinePolicyTest test = new StandardFinePolicyTest();

        test.testReturnBeforeDueDate();
        test.testReturnOnDueDate();
        test.testReturnOneDayLate();
        test.testReturnFiveDaysLate();
        test.testNegativePencePerDay();

    }

    private void testReturnBeforeDueDate() {
        StandardFinePolicy policy = new StandardFinePolicy(50);
        LocalDate due = LocalDate.of(2025, 11, 15);
        LocalDate returnDate = LocalDate.of(2025, 11, 13);

        int fine = policy.calculateFine(due, returnDate);
        if (fine == 0) {
            System.out.println("FP1 - PASS");
        }  else {
            System.out.println("FP1 - FAIL");
        }
    }

    private void testReturnOnDueDate() {
        StandardFinePolicy policy = new StandardFinePolicy(50);
        LocalDate due = LocalDate.of(2025, 11, 15);
        LocalDate returnDate = LocalDate.of(2025, 11, 15);

        int fine = policy.calculateFine(due, returnDate);
        if (fine == 0) {
            System.out.println("FP2 - PASS");
        }  else {
            System.out.println("FP2 - FAIL");
        }
    }

    private void testReturnOneDayLate() {
        StandardFinePolicy policy = new StandardFinePolicy(50);
        LocalDate due = LocalDate.of(2025, 11, 15);
        LocalDate returnDate = LocalDate.of(2025, 11, 16);

        int fine = policy.calculateFine(due, returnDate);
        if (fine == 50) {
            System.out.println("FP3 - PASS");
        }  else {
            System.out.println("FP3 - FAIL");
        }
    }

    private void testReturnFiveDaysLate() {
        StandardFinePolicy policy = new StandardFinePolicy(50);
        LocalDate due = LocalDate.of(2025, 11, 15);
        LocalDate returnDate = LocalDate.of(2025, 11, 20);

        int fine = policy.calculateFine(due, returnDate);
        if (fine == 250) {
            System.out.println("FP4 - PASS");
        }  else {
            System.out.println("FP4 - FAIL");
        }
    }

    private void testNegativePencePerDay() {
        try {
            System.out.println("FP5 - FAIL (no exception thrown)");
        } catch (IllegalArgumentException e) {
            System.out.println("FP5 - PASS (exception thrown: " + e.getMessage() + ")");
        }
    }

}
