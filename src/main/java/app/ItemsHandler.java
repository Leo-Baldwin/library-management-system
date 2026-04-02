package app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import domain.model.Book;
import domain.model.Dvd;
import domain.model.Magazine;
import domain.service.Library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsHandler extends BaseHandler {
    private final Library library;
    private final Gson gson;

    public ItemsHandler(Library library, Gson gson) {
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
        String json = gson.toJson(library.listItems());
        send(exchange, 200, json);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        JsonObject body = readJsonBody(exchange, gson, "type", "title", "categories");
        String type = body.get("type").getAsString();

        switch (type.toLowerCase()) {
            case "book" -> {
                Book book = new Book(
                        body.get("title").getAsString(),
                        body.get("author").getAsString(),
                        body.get("yearOfPublish").getAsInt(),
                        toStringList(body.getAsJsonArray("categories"))
                );
                library.addItem(book);
                send(exchange, 201, gson.toJson(book));
            }
            case "dvd" -> {
                Dvd dvd = new Dvd(
                        body.get("title").getAsString(),
                        body.get("yearOfRelease").getAsInt(),
                        body.get("durationMinutes").getAsInt(),
                        body.get("ageRating").getAsString(),
                        toStringList(body.getAsJsonArray("categories"))
                );
                library.addItem(dvd);
                send(exchange, 201, gson.toJson(dvd));
            }
            case "magazine" -> {
                Magazine magazine = new Magazine(
                        body.get("title").getAsString(),
                        body.get("publisher").getAsString(),
                        body.get("yearOfPublish").getAsInt(),
                        toStringList(body.getAsJsonArray("categories"))
                );
                library.addItem(magazine);
                send(exchange, 201, gson.toJson(magazine));
            }
            default -> sendError(exchange, 400, "Unknown item type: " + type);
        }
    }

    private List<String> toStringList(JsonArray array) {
        List<String> list = new ArrayList<>();
        if (array != null) {
            array.forEach(el -> list.add(el.getAsString()));
        }
        return list;
    }
}
