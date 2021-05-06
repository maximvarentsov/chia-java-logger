package pw.gatchina.chia.test;

import com.mongodb.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.*;
import pw.gatchina.chia.response.GetPlots;
import pw.gatchina.util.ConfigHelper;
import pw.gatchina.util.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws IOException  {
        final var config = ConfigHelper.saveAndLoad("config.json", JsonConfig.class);

        var mongo = new MongoDatabaseManager(config.mongo.connection);

        var walletBalance = ApiClient.getWalletBalance(config.hosts.wallet, 1);

        var mongoCollectionPlots = mongo.getCollection("plots", GetPlots.Plot.class);
        mongoCollectionPlots.createIndex(Indexes.ascending("plotSeed"), new IndexOptions().background(true).unique(true));

        var blockChainState = ApiClient.getBlockchainState(config.hosts.fullNode);
        var networkSpace = blockChainState.blockchainState.space;
        //logger.info(new GsonBuilder().setPrettyPrinting().create().toJson(blockChainState));

        var allPlots = new ArrayList<GetPlots.Plot>();
        logger.info("List of harvesters:");
        for (var host : config.hosts.harvesters) {
            var response = ApiClient.getPlots(host);
            var plots= Arrays.asList(response.plots);
            logger.info("host: {}", host);
            logger.info("plots: {}", response.plots.length);
            var totalPlotSize = plots.stream().mapToLong(e -> e.fileSize).sum();
            logger.info("plots size: {}", Utils.humanReadableByteCountBin(totalPlotSize));
            allPlots.addAll(plots);
        }
        logger.info("--------");
        logger.info("total plots: {}", allPlots.size());
        var totalPlotSize = allPlots.stream().mapToLong(e -> e.fileSize).sum();
        logger.info("total plots size: {}", Utils.humanReadableByteCountBin(totalPlotSize));

        var replaceOnePlots = new ArrayList<ReplaceOneModel<GetPlots.Plot>>(allPlots.size());

        for (var plot : allPlots) {
            var filter = Filters.eq("plotSeed", plot.plotSeed);
            replaceOnePlots.add(new ReplaceOneModel<>(filter, plot, new ReplaceOptions().upsert(true)));
        }
        var bulkWriteResult = mongoCollectionPlots.bulkWrite(replaceOnePlots, new BulkWriteOptions().ordered(false));

        var chanceToWin = Utils.chanceToWin(totalPlotSize, networkSpace);
        logger.info("wallet balance {} XCH", walletBalance.walletBalance.confirmedWalletBalance);
        logger.info("network size {}", Utils.humanReadableByteCountBin(networkSpace));
        logger.info("XCH per day: {}", chanceToWin);
        logger.info("XCH per week: {}", chanceToWin * 7);
        logger.info("XCH per month: {}", chanceToWin * 30);

        /*
        var plotSet = new HashSet<String>();
        for (var plot : allPlots) {
            if (plotSet.contains(plot.plotSeed)) {
                logger.info("duplicate plot " + plot.filename);
            }
            plotSet.add(plot.plotSeed);
        }
        */

        var statisticsHourly = new StatisticsHourly();
        statisticsHourly.chanceToWin = chanceToWin;
        statisticsHourly.datetime = LocalDateTime.now();
        statisticsHourly.plotsCount = allPlots.size();
        statisticsHourly.plotsCountSize = totalPlotSize;
        statisticsHourly.networkSize = networkSpace;

    }
}
