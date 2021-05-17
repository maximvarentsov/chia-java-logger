package pw.gatchina.chia.tasks;

import com.mongodb.client.model.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.*;
import pw.gatchina.chia.response.GetPlots;

import java.util.ArrayList;
import java.util.Arrays;

public class UpdatePlots implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UpdatePlots.class);

    private final MongoDatabaseManager mongo;
    private final JsonConfig config;

    public UpdatePlots(final @NotNull MongoDatabaseManager mongo, final @NotNull JsonConfig config) {
        this.mongo = mongo;
        this.config = config;
    }

    @Override
    public void run() {
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

        final var collection = mongo.getCollection("plots", GetPlots.Plot.class);

        collection.createIndex(
                Indexes.ascending("plotSeed"),
                new IndexOptions().background(true).unique(true)
        );

        if (allPlots.isEmpty()) {
            logger.warn("harvesters don't return plots");
            return;
        }

        final var replaceOnePlots = new ArrayList<ReplaceOneModel<GetPlots.Plot>>(allPlots.size());

        for (final var plot : allPlots) {
            final var filter = Filters.eq("plotSeed", plot.plotSeed);
            replaceOnePlots.add(new ReplaceOneModel<>(filter, plot, new ReplaceOptions().upsert(true)));
        }

        final var bulkWriteResult = collection.bulkWrite(replaceOnePlots, new BulkWriteOptions().ordered(false));

        logger.info("Update plots statistics success changed {} documents.", bulkWriteResult.getInsertedCount());
    }
}
