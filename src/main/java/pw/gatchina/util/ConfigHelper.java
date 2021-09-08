package pw.gatchina.util;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

public final class ConfigHelper {
    private final static ReentrantLock lock = new ReentrantLock();
    private final static Gson GSON = GsonUtil.get();
    public static String DEFAULT_CONFIG_DIR = "config";

    public static Path save(final @NotNull String src, final @NotNull String folderDir) throws IOException {
        lock.lock();
        try {
            final var configDir = Paths.get(".", folderDir);
            final var file = configDir.resolve(src);
            if (Files.notExists(file)) {
                Files.createDirectories(file.getParent());
                try (var is = ConfigHelper.class.getResourceAsStream("/" + src)) {
                    Files.copy(is, file);
                }
            }
            return file;
        } finally {
            lock.unlock();
        }
    }

    public static InputStream saveAndLoad(final @NotNull String path, final @NotNull String folderDir) throws IOException {
        final var save = save(path, folderDir);
        return new ByteArrayInputStream(Files.readAllBytes(save));
    }

    public static <T> T saveAndLoad(final @NotNull String config, final @NotNull Class<T> type) throws IOException {
        return saveAndLoad(config, DEFAULT_CONFIG_DIR, type);
    }

    public static <T> T saveAndLoad(final @NotNull String config, final @NotNull String folderDir, final @NotNull Class<T> type) throws IOException {
        final var stream = saveAndLoad(config, folderDir);
        return GSON.fromJson(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)), type);
    }
}