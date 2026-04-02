package app;

import domain.model.Book;
import domain.model.Dvd;
import domain.model.Magazine;
import domain.model.MediaItem;
import domain.policy.FinePolicy;
import domain.policy.LoanPolicy;
import domain.policy.MediaTypeFinePolicy;
import domain.policy.MediaTypeLoanPolicy;
import domain.service.Library;
import presentation.ConsoleMenu;

import java.util.HashMap;
import java.util.Map;

public class App {

    /**
     * Application entry point - Sets up the Library and runs the console UI.
     *
     * @param args allows for passing arguments to the program on the command line
     */
    public static void main(String[] args) {

        // Injects policy interfaces with their configurations and creates Library object
        Map<Class<? extends MediaItem>, Integer> loanDaysByType = new HashMap<>();
        loanDaysByType.put(Book.class, 21);
        loanDaysByType.put(Dvd.class, 7);
        loanDaysByType.put(Magazine.class, 14);
        LoanPolicy loanPolicy = new MediaTypeLoanPolicy(14, loanDaysByType);

        Map<Class<? extends MediaItem>, Integer> fineRateByType = new HashMap<>();
        fineRateByType.put(Book.class, 25);
        fineRateByType.put(Dvd.class, 100);
        fineRateByType.put(Magazine.class, 50);
        FinePolicy finePolicy = new MediaTypeFinePolicy(50, fineRateByType);
        Library library = new Library(loanPolicy, finePolicy);

        DemoDataLoader.loadDemoData(library);

        // Starts the console UI
        new ConsoleMenu(library).run();
    }
}
