package pw.gatchina.util;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class GsonUtil {
    private static final Gson gson = create(null);
    private static final Gson prettyGson = create(GsonBuilder::setPrettyPrinting);

    public static class LocalDateAdapter implements JsonSerializer<LocalDate> {
        @Override
        public JsonElement serialize(final @NotNull LocalDate date,
                                     final @NotNull Type typeOfSrc,
                                     final @NotNull JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }

    @NotNull
    public static Gson get() {
        return gson;
    }

    @NotNull
    public static Gson prettyGson() {
        return prettyGson;
    }

    @NotNull
    public static Gson create(final @Nullable Consumer<GsonBuilder> consumer) {
        final var builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

        if (consumer != null) {
            consumer.accept(builder);
        }
        return builder.create();
    }
}
