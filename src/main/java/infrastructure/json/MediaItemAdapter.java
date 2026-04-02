package infrastructure.json;

import com.google.gson.*;
import domain.model.Book;
import domain.model.Dvd;
import domain.model.Magazine;
import domain.model.MediaItem;

import java.lang.reflect.Type;

/**
 * Gson adapter that handles polymorphic serialization/deserialization of {@link MediaItem} subtypes.
 * Wraps each item with a "type" discriminator field.
 */
public class MediaItemAdapter implements JsonSerializer<MediaItem>, JsonDeserializer<MediaItem> {

    @Override
    public JsonElement serialize(MediaItem item, Type type, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", item.getClass().getSimpleName());
        wrapper.add("data", context.serialize(item, item.getClass()));
        return wrapper;
    }

    @Override
    public MediaItem deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject wrapper = json.getAsJsonObject();
        String typeName = wrapper.get("type").getAsString();
        JsonElement data = wrapper.get("data");

        return switch (typeName) {
            case "Book" -> context.deserialize(data, Book.class);
            case "Dvd" -> context.deserialize(data, Dvd.class);
            case "Magazine" -> context.deserialize(data, Magazine.class);
            default -> throw new JsonParseException("Unknown MediaItem type: " + typeName);
        };
    }
}
