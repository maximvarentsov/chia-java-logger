package pw.gatchina.chia;

import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class OkHttpClientWithKeys {

    public static OkHttpClient create(@NotNull final String rootCa, @NotNull final String privateKey, @NotNull final String privateCrt) {
        try (var trustedCertificateAsInputStream = Files.newInputStream(Paths.get(rootCa), StandardOpenOption.READ)) {
            var certificateFactory = CertificateFactory.getInstance("X.509");
            var trustedCertificate = certificateFactory.generateCertificate(trustedCertificateAsInputStream);
            var trustStore = createEmptyKeyStore("changeit".toCharArray());
            trustStore.setCertificateEntry("server-certificate", trustedCertificate);

            var privateKeyContent = Files.readString(Paths.get(privateKey), Charset.defaultCharset())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\n", "");

            var privateKeyAsBytes = Base64.getDecoder().decode(privateKeyContent);
            var keyFactory = KeyFactory.getInstance("RSA");
            var keySpec = new PKCS8EncodedKeySpec(privateKeyAsBytes);

            try (var certificateChainAsInputStream = Files.newInputStream(Paths.get(privateCrt), StandardOpenOption.READ)) {
                var certificateChain = certificateFactory.generateCertificate(certificateChainAsInputStream);

                var identityStore = createEmptyKeyStore("changeit".toCharArray());
                identityStore.setKeyEntry("client", keyFactory.generatePrivate(keySpec), "changeit".toCharArray(), new Certificate[]{certificateChain});

                var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                var trustManagers = trustManagerFactory.getTrustManagers();

                var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(identityStore, "changeit".toCharArray());
                var keyManagers = keyManagerFactory.getKeyManagers();

                var sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, trustManagers, null);

                var sslSocketFactory = sslContext.getSocketFactory();

                return new OkHttpClient.Builder()
                        .hostnameVerifier((hostname1, session) -> true)
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0])
                        .build();
            }
        } catch (IOException | InvalidKeySpecException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | CertificateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static KeyStore createEmptyKeyStore(char[] keyStorePassword)  {
        try {
            var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, keyStorePassword);
            return keyStore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
