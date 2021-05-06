package pw.gatchina.chia.tasks;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.*;
import pw.gatchina.chia.response.GetBlockchainState;

public class UpdateBlockchain implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UpdateBlockchain.class);

    private final MongoDatabaseManager mongo;
    private final JsonConfig config;

    public UpdateBlockchain(final @NotNull MongoDatabaseManager mongo, final @NotNull JsonConfig config) {
        this.mongo = mongo;
        this.config = config;
    }

    @Override
    public void run() {
        final var response = ApiClient.getBlockchainState(config.hosts.fullNode);

        if (!response.success) {
            logger.error("Update blockchain failed.");
            return;
        }

        final var collection = mongo.getCollection("blockchain", GetBlockchainState.BlockchainState.class);

        collection.createIndex(
                Indexes.ascending("peak.timestamp"),
                new IndexOptions().background(true)
        );

        collection.insertOne(response.blockchainState);

        logger.info("Update blockchain statistics success.");
    }
}
