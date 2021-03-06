package pw.gatchina.chia.scripts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.*;
import pw.gatchina.chia.response.GetPlots;
import pw.gatchina.util.Bytes;
import pw.gatchina.util.ConfigHelper;
import pw.gatchina.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class TestMain {
    private static final Logger logger = LoggerFactory.getLogger(TestMain.class);

    public static void main(String... args) throws Exception  {
        final var config = ConfigHelper.saveAndLoad("config.json", JsonConfig.class);
/*
        var mongo = new MongoDatabaseManager(config.mongo.connection);
*/
        final var walletBalance = ApiClient.getWalletBalance(config.hosts.wallet, config.wallet.id);
/*
        var mongoCollectionPlots = mongo.getCollection("plots", GetPlots.Plot.class);
        mongoCollectionPlots.createIndex(
                Indexes.ascending("plotSeed"),
                new IndexOptions().background(true).unique(true)
        );
*/
        final var blockChainState = ApiClient.getBlockchainState(config.hosts.fullNode);
        final var networkSpace = blockChainState.blockchainState.space;
        //logger.info(new GsonBuilder().setPrettyPrinting().create().toJson(blockChainState));

        final var allPlots = new ArrayList<GetPlots.Plot>();
        logger.info("List of harvesters:");
        for (final var host : config.hosts.harvesters) {
            final var response = ApiClient.getPlots(host);
            final var plots= Arrays.asList(response.plots);
            logger.info("host: {}", host);
            logger.info("plots: {}", response.plots.length);
            final var totalPlotSize = plots.stream().mapToLong(e -> e.fileSize).sum();
            logger.info("plots size: {}", Bytes.friendly(totalPlotSize));
            allPlots.addAll(plots);
        }

        logger.info("--------");
        logger.info("total plots: {}", allPlots.size());
        final var totalPlotSize = allPlots.stream().mapToLong(e -> e.fileSize).sum();
        logger.info("total plots size: {}", Bytes.friendly(totalPlotSize));
/*
        var replaceOnePlots = new ArrayList<ReplaceOneModel<GetPlots.Plot>>(allPlots.size());
        for (var plot : allPlots) {
            var filter = Filters.eq("plotSeed", plot.plotSeed);
            replaceOnePlots.add(new ReplaceOneModel<>(filter, plot, new ReplaceOptions().upsert(true)));
        }
        var bulkWriteResult = mongoCollectionPlots.bulkWrite(replaceOnePlots, new BulkWriteOptions().ordered(false));
*/
        final var chanceToWin = Utils.chanceToWin(totalPlotSize, networkSpace);
        logger.info("wallet balance {} XCH", Utils.mojoToChia(walletBalance.walletBalance.confirmedWalletBalance));
        logger.info("network size {}", Bytes.friendly(Bytes.B, networkSpace));
        logger.info("XCH per day: {}", chanceToWin);
        logger.info("XCH per week: {}", chanceToWin * 7);
        logger.info("XCH per month: {}", chanceToWin * 30);

        final var plotsSet = new HashSet<String>();
        for (final var plot : allPlots) {
            if (!plotsSet.add(plot.plotSeed)) {
                logger.info("duplicate plot " + plot.filename);
            }
        }
    }
}
