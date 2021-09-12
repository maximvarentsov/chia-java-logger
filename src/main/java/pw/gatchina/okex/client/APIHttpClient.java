package pw.gatchina.okex.client;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import pw.gatchina.okex.APIConfiguration;
import pw.gatchina.okex.utils.HmacSHA256Base64Utils;
import pw.gatchina.okex.utils.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class APIHttpClient {
    private final APIConfiguration config;
    private final APICredentials credentials;

    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneId.of("UTC"));

    public APIHttpClient(final APIConfiguration config, final APICredentials credentials) {
        this.config = config;
        this.credentials = credentials;
    }

    /**
     * Get a ok http 3 client object.
     * Declare:
     *  1. Set default client args:
     *         connectTimeout=30s
     *         readTimeout=30s
     *         writeTimeout=30s
     *         retryOnConnectionFailure=true.
     *  2. Set request headers:
     *      Content-Type: application/json; charset=UTF-8  (default)
     *      Cookie: locale=en_US        (English)
     *      OK-ACCESS-KEY: (Your setting)
     *      OK-ACCESS-SIGN: (Use your setting, auto sign and add)
     *      OK-ACCESS-TIMESTAMP: (Auto add)
     *      OK-ACCESS-PASSPHRASE: Your setting
     *  3. Set default print api info: false.
     */
    public OkHttpClient client() {
        final var builder = new OkHttpClient.Builder();
        builder.connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS);
        builder.readTimeout(config.getReadTimeout(), TimeUnit.SECONDS);
        builder.writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS);

        builder.retryOnConnectionFailure(config.isRetryOnConnectionFailure());

        builder.addInterceptor((Interceptor.Chain chain) -> {
            final var requestBuilder = chain.request().newBuilder();
            final var timestamp = DATE_TIME_FORMATTER.format(Instant.now());
            requestBuilder.headers(headers(chain.request(), timestamp));
            final var request = requestBuilder.build();
            return chain.proceed(request);
        });
        return builder.build();
    }

    private Headers headers(final @NotNull Request request, final @NotNull String timestamp) {
        final var builder = new Headers.Builder();
        builder.add("Accept", "application/json");
        builder.add("Content-Type", "application/json; charset=UTF-8");
        builder.add("Cookie", "locale=en_US");

        if (StringUtils.isNotEmpty(credentials.getSecretKey())) {
            builder.add("OK-ACCESS-KEY", credentials.getApiKey());
            builder.add("OK-ACCESS-SIGN", sign(request, timestamp));
            builder.add("OK-ACCESS-TIMESTAMP", timestamp);
            builder.add("OK-ACCESS-PASSPHRASE", credentials.getPassphrase());
        }

        return builder.build();
    }

    private String sign(final @NotNull Request request, final @NotNull String timestamp) {
        final String sign;

        try {
            sign = HmacSHA256Base64Utils.sign(timestamp, this.method(request), this.requestPath(request),
                    this.queryString(request), this.body(request), this.credentials.getSecretKey());
        } catch (final IOException e) {
            throw new APIException("Request get body io exception.", e);
        } catch (final CloneNotSupportedException e) {
            throw new APIException("Hmac SHA256 Base64 Signature clone not supported exception.", e);
        } catch (final InvalidKeyException e) {
            throw new APIException("Hmac SHA256 Base64 Signature invalid key exception.", e);
        }
        return sign;
    }

    private String url(final @NotNull Request request) {
        return request.url().toString();
    }

    private String method(final @NotNull Request request) {
        return request.method().toUpperCase();
    }

    private String requestPath(final @NotNull Request request) {
        var url = url(request);
        url = url.replace(config.getEndpoint(), "");
        var requestPath = url;
        if (requestPath.contains("?")) {
            requestPath = requestPath.substring(0, url.lastIndexOf("?"));
        }
        if (config.getEndpoint().endsWith("/")){
            requestPath = "/" + requestPath;
        }
        return requestPath;
    }

    private String queryString(final Request request) {
        final var url = this.url(request);
        request.body();
        var queryString = "";

        if (url.contains("?")) {
            queryString = url.substring(url.lastIndexOf("?") + 1);
        }

        return queryString;
    }

    private String body(final @NotNull Request request) throws IOException {
        final var requestBody = request.body();
        var body = "";
        if (requestBody != null) {
            final var buffer = new Buffer();
            requestBody.writeTo(buffer);
            body = buffer.readString(Charset.defaultCharset());
        }
        return body;
    }
}
