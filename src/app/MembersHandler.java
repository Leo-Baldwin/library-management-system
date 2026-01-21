package app;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.service.Library;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MembersHandler implements HttpHandler {
    private final Library library;
    private final Gson gson;

    public MembersHandler(Library library, Gson gson) {
        this.library = library;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        String json = gson.toJson(library.listMembers());
        send(exchange, 200, json);
    }

    private void send(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
