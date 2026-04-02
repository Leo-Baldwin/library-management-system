package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import domain.model.Reservation;
import domain.service.Library;

import java.io.IOException;
import java.util.UUID;

public class ReservationsHandler extends BaseHandler {
    private final Library library;
    private final Gson gson;

    public ReservationsHandler(Library library, Gson gson) {
        this.library = library;
        this.gson = gson;
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendMethodNotAllowed(exchange);
            return;
        }

        JsonObject body = readJsonBody(exchange, gson, "memberId", "mediaId");
        UUID memberId = UUID.fromString(body.get("memberId").getAsString());
        UUID mediaId = UUID.fromString(body.get("mediaId").getAsString());

        Reservation reservation = library.placeReservation(memberId, mediaId);
        send(exchange, 201, gson.toJson(reservation));
    }
}
