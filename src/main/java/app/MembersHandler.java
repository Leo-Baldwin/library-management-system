package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import domain.model.Member;
import domain.service.Library;

import java.io.IOException;

public class MembersHandler extends BaseHandler {
    private final Library library;
    private final Gson gson;

    public MembersHandler(Library library, Gson gson) {
        this.library = library;
        this.gson = gson;
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod().toUpperCase()) {
            case "GET" -> handleGet(exchange);
            case "POST" -> handlePost(exchange);
            default -> sendMethodNotAllowed(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String json = gson.toJson(library.listMembers());
        send(exchange, 200, json);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        JsonObject body = readJsonBody(exchange, gson, "name", "email");
        Member member = new Member(
                body.get("name").getAsString(),
                body.get("email").getAsString()
        );
        library.addMember(member);
        send(exchange, 201, gson.toJson(member));
    }
}
