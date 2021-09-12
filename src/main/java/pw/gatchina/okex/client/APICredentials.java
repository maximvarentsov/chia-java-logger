package pw.gatchina.okex.client;

import org.jetbrains.annotations.NotNull;
import pw.gatchina.okex.APIConfiguration;

public class APICredentials {
    /**
     * The user's secret key provided by OKEx.
     */
    public final String apiKey;
    /**
     * The private key used to sign your request data.
     */
    public final String secretKey;
    /**
     * The Passphrase will be provided by you to further secure your API access.
     */
    public final String passphrase;

    public APICredentials(final @NotNull APIConfiguration config) {
        apiKey = config.getApiKey();
        secretKey = config.getSecretKey();
        passphrase = config.getPassphrase();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPassphrase() {
        return passphrase;
    }
}
