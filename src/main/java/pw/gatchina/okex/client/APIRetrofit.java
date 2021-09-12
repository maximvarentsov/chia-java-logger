package pw.gatchina.okex.client;

import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import pw.gatchina.okex.APIConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIRetrofit {
    private final APIConfiguration config;
    private final OkHttpClient client;

    public APIRetrofit(final @NotNull APIConfiguration config, final @NotNull OkHttpClient client) {
        this.config = config;
        this.client = client;
    }

    public Retrofit retrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(client);
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.baseUrl(config.getEndpoint());
        return builder.build();
    }
}
