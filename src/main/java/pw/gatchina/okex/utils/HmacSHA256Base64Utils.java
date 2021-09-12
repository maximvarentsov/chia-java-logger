package pw.gatchina.okex.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Hmac SHA256 Base64 Signature Utils.
 *
 * @author Tony Tian
 * @version 1.0.0
 */
public class HmacSHA256Base64Utils {
    public static Mac MAC;

    static {
        try {
            MAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Signing a Message.
     * using: Hmac SHA256 + base64
     *
     * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal values are allowed.
     *                    eg: 2018-03-08T10:59:25.789Z
     * @param method      eg: POST
     * @param requestPath eg: /orders
     * @param queryString eg: before=2&limit=30
     * @param body        json string, eg: {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
     * @param secretKey   user's secret key eg: E65791902180E9EF4510DB6A77F6EBAE
     * @return signed string   eg: TO6uwdqz+31SIPkd4I+9NiZGmVH74dXi+Fd5X0EzzSQ=
     */
    public static String sign(String timestamp, String method, String requestPath,
                              String queryString, String body, String secretKey)
            throws CloneNotSupportedException, InvalidKeyException, UnsupportedEncodingException {

        if (StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(method)) {
            return "";
        }

        final var preHash = preHash(timestamp, method, requestPath, queryString, body);
        final var secretKeyBytes = secretKey.getBytes(Charset.defaultCharset());
        final var secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
        final var mac = (Mac) MAC.clone();
        mac.init(secretKeySpec);

        return Base64.getEncoder().encodeToString(mac.doFinal(preHash.getBytes(Charset.defaultCharset())));
    }

    /**
     * the prehash string = timestamp + method + requestPath + body .<br/>
     *
     * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal values are allowed.
     *                    eg: 2018-03-08T10:59:25.789Z
     * @param method      eg: POST
     * @param requestPath eg: /orders
     * @param queryString eg: before=2&limit=30
     * @param body        json string, eg: {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
     * @return prehash string eg: 2018-03-08T10:59:25.789ZPOST/orders?before=2&limit=30{"product_id":"BTC-USD-0309",
     * "order_id":"377454671037440"}
     */
    public static String preHash(String timestamp, String method, String requestPath, String queryString, String body) {
        final var preHash = new StringBuilder();
        preHash.append(timestamp);
        preHash.append(method.toUpperCase());
        preHash.append(requestPath);

        if (StringUtils.isNotEmpty(queryString)) {
            preHash.append("?").append(queryString);
        }

        if (StringUtils.isNotEmpty(body)) {
            preHash.append(body);
        }

        return preHash.toString();
    }
}
