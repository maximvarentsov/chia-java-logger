package pw.gatchina.chia.tasks;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.ApiClient;
import pw.gatchina.chia.JsonConfig;
import pw.gatchina.chia.MongoDatabaseManager;
import pw.gatchina.chia.Statistics;
import pw.gatchina.chia.response.GetPlots;
import pw.gatchina.util.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class UpdateStatistics implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UpdateStatistics.class);

    private final MongoDatabaseManager mongo;
    private final JsonConfig config;

    public UpdateStatistics(final @NotNull MongoDatabaseManager mongo, final @NotNull JsonConfig config) {
        this.mongo = mongo;
        this.config = config;
    }

    @Override
    public void run() {
        final var blockChainState = ApiClient.getBlockchainState(config.hosts.fullNode);
        if (!blockChainState.success) {
            logger.error("Update blockchain failed.");
            return;
        }

        final var networkSpace = blockChainState.blockchainState.space;

        final var allPlots = new ArrayList<GetPlots.Plot>();
        for (final var host : config.hosts.harvesters) {
            final var response = ApiClient.getPlots(host);
            if (response.success) {
                final var plots = Arrays.asList(response.plots);
                allPlots.addAll(plots);
            } else {
                logger.error("harvester {} response not success", host);
            }
        }
        final var totalPlotSize = allPlots.stream().mapToLong(e -> e.fileSize).sum();
        final var chanceToWin = Utils.chanceToWin(totalPlotSize, networkSpace);

        final var statistics = new Statistics();
        statistics.chanceToWin = chanceToWin;
        statistics.datetime = LocalDateTime.now();
        statistics.plotsCount = allPlots.size();
        statistics.plotsCountSize = totalPlotSize;
        statistics.networkSize = networkSpace;

        final var collection = mongo.getCollection("statistics", Statistics.class);

        collection.createIndex(
                Indexes.ascending("datetime"),
                new IndexOptions().background(true)
        );

        collection.insertOne(statistics);

        logger.info("Update statistics success.");
    }
}
