package infrastructure.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.model.MediaItem;
import domain.service.Library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Handles saving and loading library state to/from a JSON file.
 */
public class JsonPersistence {

    private final Path filePath;
    private final Gson gson;

    public JsonPersistence(Path filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(MediaItem.class, new MediaItemAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    /**
     * Saves the current library state to the JSON file.
     */
    public void save(Library library) throws IOException {
        LibraryState state = new LibraryState(
                library.listItems(),
                library.listMembers(),
                library.listLoans(),
                library.listReservations()
        );
        Files.writeString(filePath, gson.toJson(state));
    }

    /**
     * Loads library state from the JSON file if it exists.
     *
     * @return the loaded state, or null if the file does not exist
     */
    public LibraryState load() throws IOException {
        if (!Files.exists(filePath)) {
            return null;
        }
        String json = Files.readString(filePath);
        return gson.fromJson(json, LibraryState.class);
    }

    /**
     * @return a Gson instance configured with the same type adapters, for use in HTTP handlers.
     */
    public Gson getGson() {
        return gson;
    }
}
