package pw.gatchina.okex;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class APIConfiguration {
    /**
     * The user's api key provided by OKEx.
     */
    private String apiKey;
    /**
     * The user's secret key provided by OKEx. The secret key used to sign your request data.
     */
    private String secretKey;
    /**
     * The Passphrase will be provided by you to further secure your API access.
     */
    private String passphrase;
    /**
     * Rest api endpoint url.
     */
    private String endpoint;
    /**
     * Host connection timeout.
     */
    private final long connectTimeout;
    /**
     * The host reads the information timeout.
     */
    private final long readTimeout;
    /**
     * The host writes the information timeout.
     */
    private final long writeTimeout;
    /**
     * Failure reconnection, default true.
     */
    private boolean retryOnConnectionFailure;

    public APIConfiguration() {
        this(null);
    }

    public APIConfiguration(final @Nullable String endpoint) {
        this.apiKey = null;
        this.secretKey = null;
        this.passphrase = null;
        this.endpoint = endpoint;
        this.connectTimeout = TimeUnit.SECONDS.toMillis(30);
        this.readTimeout = TimeUnit.SECONDS.toMillis(30);
        this.writeTimeout = TimeUnit.SECONDS.toMillis(30);
        this.retryOnConnectionFailure = true;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final @NotNull String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final @NotNull String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(final @NotNull String passphrase) {
        this.passphrase = passphrase;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final @NotNull String endpoint) {
        this.endpoint = endpoint;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public void setRetryOnConnectionFailure(final boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }
}
