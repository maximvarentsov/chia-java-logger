package pw.gatchina.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

public final class ConfigHelper {
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface OverrideProperty {
        public String name();
    }

    public static String DEFAULT_CONFIG_DIR = "config";

    private static final ReentrantLock lock = new ReentrantLock();

    public static Path save(String src, String folderDir) throws IOException {
        lock.lock();
        try {
            Path configDir = Paths.get(".", folderDir);
            Path file = configDir.resolve(src);
            if (Files.notExists(file)) {
                Files.createDirectories(file.getParent());
                try (InputStream is = ConfigHelper.class.getResourceAsStream("/" + src)) {
                    Files.copy(is, file);
                }
            }
            return file;
        }
        finally {
            lock.unlock();
        }
    }

    public static Path save(String src) throws IOException {
        return save(src, DEFAULT_CONFIG_DIR);
    }

    public static Reader saveAndLoadUtf8(String path) throws IOException {
        return new BufferedReader(new InputStreamReader(saveAndLoad(path), StandardCharsets.UTF_8));
    }

    public static InputStream saveAndLoad(String path) throws IOException {
        Path save = save(path);
        return new ByteArrayInputStream(Files.readAllBytes(save));
    }

    public static InputStream saveAndLoad(String path, String folderDir) throws IOException {
        Path save = save(path, folderDir);
        return new ByteArrayInputStream(Files.readAllBytes(save));
    }

    public static <T> T saveAndLoad(final String config, Class<T> type) throws IOException {
        return saveAndLoad(config, DEFAULT_CONFIG_DIR, type);
    }

    public static <T> T saveAndLoad(final String config, final String folderDir, Class<T> type) throws IOException {
        InputStream stream = saveAndLoad(config, folderDir);
        return override(GSON.fromJson(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)), type), type);
    }

    public static <T> T override(T obj, Class<T> type) {
        for (Field field : type.getDeclaredFields()) {
            OverrideProperty prop = field.getAnnotation(OverrideProperty.class);
            if (prop == null)
                continue;

            String oValue = System.getProperty(prop.name());
            if (oValue == null)
                continue;

            try {
                if (field.getType() == String.class)
                    field.set(obj, oValue);
                else if (field.getType() == Integer.class)
                    field.set(obj, Integer.valueOf(oValue));
                else if (field.getType() == int.class)
                    field.setInt(obj, Integer.parseInt(oValue));
                else if (field.getType() == Long.class)
                    field.set(obj, Long.valueOf(oValue));
                else if (field.getType() == long.class)
                    field.setLong(obj, Long.parseLong(oValue));
                else if (field.getType() == Boolean.class)
                    field.set(obj, Boolean.valueOf(oValue));
                else if (field.getType() == boolean.class)
                    field.setBoolean(obj, Boolean.parseBoolean(oValue));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }
}

