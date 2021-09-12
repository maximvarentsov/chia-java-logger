package pw.gatchina.okex.client;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import pw.gatchina.okex.APIConfiguration;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

public class APIClient {
    private final Retrofit retrofit;
    private final Gson gson = new Gson();

    public APIClient(final @NotNull APIConfiguration config) {
        var credentials = new APICredentials(config);
        var client = new APIHttpClient(config, credentials).client();
        retrofit = new APIRetrofit(config, client).retrofit();
    }

    public <T> T createService(final Class<T> service) {
        return retrofit.create(service);
    }

    public <T> T executeSync(final Call<T> call) {
        try {
            final var response = call.execute();

            if (response.isSuccessful()) {
                return response.body();
            } else  {
                final var reader = response.errorBody().charStream();
                final var result = gson.fromJson(reader, HttpResult.class);
                throw new APIException(result.code, result.msg);
            }
        } catch (final IOException e) {
            throw new APIException("APIClient executeSync exception.", e);
        }
    }
}
