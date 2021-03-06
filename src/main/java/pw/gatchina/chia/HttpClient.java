package pw.gatchina.chia;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import pw.gatchina.util.GsonUtil;
import pw.gatchina.util.OkHttpClientWithKeys;

import java.io.IOException;
import java.util.Map;

public class HttpClient {
    private final static Gson GSON = GsonUtil.get();
    private final static MediaType JSOM_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client;

    public HttpClient(final @NotNull String rootCa, final @NotNull String privateKey, final @NotNull String privateCrt) {
        client = OkHttpClientWithKeys.create(rootCa, privateKey, privateCrt);
    }

    public <T> T postSync(final @NotNull Class<T> type, final @NotNull String url) {
        return postSync(type, url, Map.of());
    }

    public <T> T postSync(final @NotNull Class<T> type, final @NotNull String url, final @NotNull Map<?, ?> map) {
        final var jsonMap = GSON.toJson(map);
        final var request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonMap, JSOM_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .build();
        final var call = client.newCall(request);
        try {
            final var response = call.execute();
            final var body = response.body();
            if (body == null) {
                throw new RuntimeException("response body is null");
            }
            return GSON.fromJson(body.string(), type);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
