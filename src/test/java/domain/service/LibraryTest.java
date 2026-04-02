package domain.service;

import common.ValidationException;
import domain.model.*;
import domain.policy.StandardFinePolicy;
import domain.policy.StandardLoanPolicy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library library;
    private Member member;
    private Book book;

    @BeforeEach
    void setUp() {
        library = new Library(new StandardLoanPolicy(14), new StandardFinePolicy(50));
        member = new Member("Alice", "alice@example.com");
        book = new Book("Clean Code", "Robert Martin", 2008, List.of("Software"));
        library.addMember(member);
        library.addItem(book);
    }

    // ---- Loan lifecycle ----

    @Test
    void loanItemSetsStatusToOnLoan() {
        library.loanItem(member.getId(), book.getMediaId());

        assertEquals(AvailabilityStatus.ON_LOAN, book.getStatus());
    }

    @Test
    void loanItemReturnsLoanWithCorrectIds() {
        Loan loan = library.loanItem(member.getId(), book.getMediaId());

        assertEquals(member.getId(), loan.getMemberId());
        assertEquals(book.getMediaId(), loan.getMediaId());
        assertEquals(LoanStatus.OUTSTANDING, loan.getStatus());
    }

    @Test
    void returnItemSetsStatusBackToAvailable() {
        library.loanItem(member.getId(), book.getMediaId());

        library.returnItem(book.getMediaId());

        assertEquals(AvailabilityStatus.AVAILABLE, book.getStatus());
    }

    @Test
    void returnItemMarksLoanAsReturned() {
        library.loanItem(member.getId(), book.getMediaId());

        Loan returned = library.returnItem(book.getMediaId());

        assertEquals(LoanStatus.RETURNED, returned.getStatus());
        assertNotNull(returned.getReturnDate());
    }

    // ---- Loan invariants ----

    @Test
    void cannotLoanUnavailableItem() {
        library.loanItem(member.getId(), book.getMediaId());

        Member other = new Member("Bob", "bob@example.com");
        library.addMember(other);

        assertThrows(ValidationException.class,
                () -> library.loanItem(other.getId(), book.getMediaId()));
    }

    @Test
    void cannotLoanToInactiveMember() {
        member.setActiveMember(false);

        assertThrows(ValidationException.class,
                () -> library.loanItem(member.getId(), book.getMediaId()));
    }

    // ---- Reservations ----

    @Test
    void placeReservationCreatesActiveReservation() {
        Reservation reservation = library.placeReservation(member.getId(), book.getMediaId());

        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
        assertEquals(member.getId(), reservation.getMemberId());
    }

    @Test
    void inactiveMemberCannotReserve() {
        member.setActiveMember(false);

        assertThrows(ValidationException.class,
                () -> library.placeReservation(member.getId(), book.getMediaId()));
    }

    @Test
    void returnItemWithReservationSetsStatusToReserved() {
        library.loanItem(member.getId(), book.getMediaId());

        Member other = new Member("Bob", "bob@example.com");
        library.addMember(other);
        library.placeReservation(other.getId(), book.getMediaId());

        library.returnItem(book.getMediaId());

        assertEquals(AvailabilityStatus.RESERVED, book.getStatus());
    }

    // ---- Search ----

    @Test
    void searchMediaByTitleFindsItem() {
        List<MediaItem> results = library.searchMedia("Clean");

        assertEquals(1, results.size());
        assertEquals("Clean Code", results.get(0).getTitle());
    }

    @Test
    void searchMediaByAuthorFindsBook() {
        List<MediaItem> results = library.searchMedia("Robert");

        assertEquals(1, results.size());
    }

    @Test
    void searchMediaIsCaseInsensitive() {
        List<MediaItem> results = library.searchMedia("clean code");

        assertEquals(1, results.size());
    }

    @Test
    void searchMembersFindsMatchByName() {
        List<Member> results = library.searchMembers("Alice");

        assertEquals(1, results.size());
        assertEquals("Alice", results.get(0).getName());
    }

    // ---- Item / Member removal ----

    @Test
    void removeAvailableItemSucceeds() {
        library.removeItem(book.getMediaId());

        assertTrue(library.listItems().isEmpty());
    }

    @Test
    void cannotRemoveItemOnLoan() {
        library.loanItem(member.getId(), book.getMediaId());

        assertThrows(ValidationException.class, () -> library.removeItem(book.getMediaId()));
    }
}
