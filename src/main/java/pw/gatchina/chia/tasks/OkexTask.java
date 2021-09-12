package pw.gatchina.chia.tasks;

import com.mongodb.client.MongoCollection;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.JsonConfig;
import pw.gatchina.chia.MongoDatabaseManager;
import pw.gatchina.okex.APIConfiguration;
import pw.gatchina.okex.models.InstrumentModel;
import pw.gatchina.okex.service.Instruments;
import pw.gatchina.okex.service.PublicDataAPIService;

import java.util.ArrayList;
import java.util.List;

public class OkexTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(OkexTask.class);

    private final APIConfiguration apiConfiguration = new APIConfiguration();
    private final List<String> instruments;
    private final MongoCollection<InstrumentModel> collection;

    public OkexTask(final @NotNull MongoDatabaseManager mongo, final @NotNull JsonConfig config) {
        collection  = mongo.getCollection("instruments", InstrumentModel.class);
        instruments = config.okex.instruments;
        apiConfiguration.setEndpoint(config.okex.endpoint);
        apiConfiguration.setApiKey(config.okex.apikey);
        apiConfiguration.setSecretKey(config.okex.secretkey);
        apiConfiguration.setPassphrase(config.okex.passphrase);
        apiConfiguration.setRetryOnConnectionFailure(false);
    }

    @Override
    public void run() {
        final var publicData = new PublicDataAPIService(apiConfiguration);
        final var type = Instruments.SWAP;

        for (final var instrument : instruments) {
            final var markPrice = publicData.getMarkPrice(type, null, instrument);

            if (markPrice.data.isEmpty()) {
                logger.warn("instrument {} api response is null", instrument);
                continue;
            }

            final var result = new ArrayList<InstrumentModel>();

            for (final var data : markPrice.data) {
                final var instrumentModel = new InstrumentModel();
                instrumentModel.instrument = instrument;
                instrumentModel.type = type;
                instrumentModel.value = data.markPx;

                result.add(instrumentModel);

                try {
                    Thread.sleep(250L);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex.getMessage(), ex);
                }
            }

            collection.insertMany(result);
        }
    }
}
