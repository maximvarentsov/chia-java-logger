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

public class ApiCaller {
    private final static Gson GSON = GsonUtil.get();
    private final static MediaType JSOM_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client;

    public ApiCaller(final @NotNull String rootCa, final @NotNull String privateKey, final @NotNull String privateCrt) {
        client = OkHttpClientWithKeys.create(rootCa, privateKey, privateCrt);
    }

    public <T> T callSync(final @NotNull Class<T> type, final @NotNull String url) {
        return callSync(type, url, Map.of());
    }

    public <T> T callSync(final @NotNull Class<T> type, final @NotNull String url, final @NotNull Map<?, ?> params) {
        final var jsonParams = GSON.toJson(params);
        final var request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonParams, JSOM_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .build();
        final var call = client.newCall(request);
        try {
            final var response = call.execute();
            final var body = response.body();
            if (body == null) {
                throw new RuntimeException("response body null");
            }
            return GSON.fromJson(body.string(), type);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
