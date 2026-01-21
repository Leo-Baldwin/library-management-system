package app;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import domain.model.Book;
import domain.model.Dvd;
import domain.model.Magazine;
import domain.model.MediaItem;
import domain.policy.FinePolicy;
import domain.policy.LoanPolicy;
import domain.policy.MediaTypeFinePolicy;
import domain.policy.MediaTypeLoanPolicy;
import domain.service.Library;

public class WebServer {

    public static void main(String[] args) throws Exception {
        
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

        Gson gson = new Gson();
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/items", new ItemsHandler(library, gson));
        server.createContext("/api/members", new MembersHandler(library, gson));
        server.start();

        System.out.println("Server started on port 8080");
    }
}
