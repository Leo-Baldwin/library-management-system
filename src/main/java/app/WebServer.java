package app;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import domain.policy.FinePolicy;
import domain.policy.LoanPolicy;
import domain.policy.MediaTypeFinePolicy;
import domain.policy.MediaTypeLoanPolicy;
import domain.service.Library;
import infrastructure.json.JsonPersistence;
import infrastructure.json.LibraryState;

public class WebServer {

    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());
    private static final Path DATA_FILE = Path.of("library-data.json");

    public static void main(String[] args) throws Exception {

        AppConfig config = new AppConfig();

        // Injects policy interfaces with their configurations and creates Library object
        LoanPolicy loanPolicy = new MediaTypeLoanPolicy(config.getDefaultLoanDays(), config.getLoanDaysByType());
        FinePolicy finePolicy = new MediaTypeFinePolicy(config.getDefaultFinePencePerDay(), config.getFineRatesByType());
        Library library = new Library(loanPolicy, finePolicy);

        // Load persisted data, or fall back to demo data
        JsonPersistence persistence = new JsonPersistence(DATA_FILE);
        LibraryState saved = persistence.load();
        if (saved != null) {
            saved.getItems().forEach(library::addItem);
            saved.getMembers().forEach(library::addMember);
            saved.getLoans().forEach(library::loadLoan);
            saved.getReservations().forEach(library::loadReservation);
            LOGGER.info("Loaded library state from " + DATA_FILE);
        } else {
            DemoDataLoader.loadDemoData(library);
            LOGGER.info("No saved data found, loaded demo data");
        }

        Gson gson = persistence.getGson();

        int port = config.getServerPort();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/items", new ItemsHandler(library, gson));
        server.createContext("/api/members", new MembersHandler(library, gson));
        server.createContext("/api/loans", new LoansHandler(library, gson));
        server.createContext("/api/returns", new ReturnsHandler(library, gson));
        server.createContext("/api/reservations", new ReservationsHandler(library, gson));
        server.start();

        // Save state on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                persistence.save(library);
                LOGGER.info("Library state saved to " + DATA_FILE);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to save library state", e);
            }
            server.stop(0);
        }));

        LOGGER.info("Server started on port " + port);
    }
}
