package pw.gatchina.chia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import pw.gatchina.util.OkHttpClientWithKeys;

import java.io.IOException;
import java.util.Map;

public class ApiCaller {
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final static MediaType JSOM_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private final String rootCa;
    private final String privateKey;
    private final String privateCrt;

    public ApiCaller(@NotNull final String rootCa, @NotNull final String privateKey, @NotNull final String privateCrt) {
        this.rootCa = rootCa;
        this.privateKey = privateKey;
        this.privateCrt = privateCrt;
    }

    public <T> T call(@NotNull final Class<T> type, @NotNull final String url) {
        return call(type, url, Map.of());
    }
    public <T> T call(@NotNull final Class<T> type, @NotNull final String url, @NotNull final Map<?, ?> params) {
        var jsonParams = GSON.toJson(params);
        var request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonParams, JSOM_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .build();
        var client = OkHttpClientWithKeys.create(rootCa, privateKey, privateCrt);
        var call = client.newCall(request);
        try {
            var response = call.execute();
            var body = response.body().string();
            //System.out.println(body);
            return GSON.fromJson(body, type);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
