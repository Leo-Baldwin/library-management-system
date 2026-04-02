package app;

import common.ValidationException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base HTTP handler that provides shared response utilities and error handling.
 */
public abstract class BaseHandler implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(BaseHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        LOGGER.info(() -> method + " " + path);
        try {
            handleRequest(exchange);
        } catch (ValidationException e) {
            LOGGER.warning(() -> method + " " + path + " -> 400: " + e.getMessage());
            sendError(exchange, 400, e.getMessage());
        } catch (JsonSyntaxException e) {
            LOGGER.warning(() -> method + " " + path + " -> 400: Invalid JSON");
            sendError(exchange, 400, "Invalid JSON in request body");
        } catch (IllegalArgumentException e) {
            LOGGER.warning(() -> method + " " + path + " -> 400: " + e.getMessage());
            sendError(exchange, 400, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, method + " " + path + " -> 500", e);
            sendError(exchange, 500, "Internal server error");
        }
    }

    protected abstract void handleRequest(HttpExchange exchange) throws IOException;

    protected void send(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected void sendError(HttpExchange exchange, int status, String message) throws IOException {
        send(exchange, status, "{\"error\":\"" + message.replace("\"", "\\\"") + "\"}");
    }

    protected void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        sendError(exchange, 405, "Method not allowed");
    }

    protected String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected JsonObject readJsonBody(HttpExchange exchange, Gson gson, String... requiredFields) throws IOException {
        String raw = readBody(exchange);
        if (raw == null || raw.isBlank()) {
            throw new ValidationException("Request body is empty");
        }
        JsonObject body = gson.fromJson(raw, JsonObject.class);
        if (body == null) {
            throw new ValidationException("Request body is empty");
        }
        for (String field : requiredFields) {
            if (!body.has(field) || body.get(field).isJsonNull()) {
                throw new ValidationException("Missing required field: " + field);
            }
        }
        return body;
    }
}
