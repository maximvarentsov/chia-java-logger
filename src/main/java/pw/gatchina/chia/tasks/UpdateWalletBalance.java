package pw.gatchina.chia.tasks;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.ApiClient;
import pw.gatchina.chia.response.GetWalletBalance;
import pw.gatchina.chia.JsonConfig;
import pw.gatchina.chia.MongoDatabaseManager;

public class UpdateWalletBalance implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UpdateWalletBalance.class);

    private final MongoDatabaseManager mongo;
    private final JsonConfig config;

    public UpdateWalletBalance(final @NotNull MongoDatabaseManager mongo, final @NotNull JsonConfig config) {
        this.mongo = mongo;
        this.config = config;
    }

    @Override
    public void run() {
        final var walletId = config.wallet.id;
        final var response = ApiClient.getWalletBalance(config.hosts.wallet, walletId);

        if (!response.success) {
            logger.error("Update wallet balance failed.");
            return;
        }

        final var collection = mongo.getCollection("wallets", GetWalletBalance.WalletBalance.class);

        collection.createIndex(
                Indexes.ascending("walletId"),
                new IndexOptions().unique(true).background(true)
        );

        collection.replaceOne(
                Filters.eq("walletId", walletId),
                response.walletBalance,
                new ReplaceOptions().upsert(true)
        );

        logger.info("Update wallet balance success.");
    }
}
