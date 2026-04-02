package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import domain.model.Loan;
import domain.service.Library;

import java.io.IOException;
import java.util.UUID;

public class ReturnsHandler extends BaseHandler {
    private final Library library;
    private final Gson gson;

    public ReturnsHandler(Library library, Gson gson) {
        this.library = library;
        this.gson = gson;
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendMethodNotAllowed(exchange);
            return;
        }

        JsonObject body = readJsonBody(exchange, gson, "mediaId");
        UUID mediaId = UUID.fromString(body.get("mediaId").getAsString());

        Loan loan = library.returnItem(mediaId);
        send(exchange, 200, gson.toJson(loan));
    }
}
