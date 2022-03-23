package pw.gatchina.chia.scripts;

import pw.gatchina.chia.JsonConfig;
import pw.gatchina.okex.APIConfiguration;
import pw.gatchina.okex.service.Instruments;
import pw.gatchina.okex.service.PublicDataAPIService;
import pw.gatchina.util.ConfigHelper;

/**
 * @author maximvarentsov
 * @since 07.08.2021
 */
public class OkexMain {
    /**
     * https://www.okex.com/docs-v5/
     */
    public static void main(String[] args) throws Exception {
        final var config = ConfigHelper.saveAndLoad("config.json", JsonConfig.class);

        final var apiConfiguration = new APIConfiguration();
        apiConfiguration.setEndpoint(config.okex.endpoint);
        apiConfiguration.setApiKey(config.okex.apikey);
        apiConfiguration.setSecretKey(config.okex.secretkey);
        apiConfiguration.setPassphrase(config.okex.passphrase);
        apiConfiguration.setRetryOnConnectionFailure(false);

        final var publicData = new PublicDataAPIService(apiConfiguration);

        final var response = publicData.getMarkPrice(Instruments.SWAP, null, "XCH-USDT");

        response.data.forEach(System.out::println);
    }
}
