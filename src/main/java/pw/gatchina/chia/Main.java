package pw.gatchina.chia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.tasks.*;
import pw.gatchina.okex.utils.StringUtils;
import pw.gatchina.util.ConfigHelper;
import pw.gatchina.util.CronScheduler;
import pw.gatchina.util.StaticShutdownCallbackRegistry;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    static {
        System.setProperty("log4j.shutdownCallbackRegistry", StaticShutdownCallbackRegistry.class.getCanonicalName());
    }

    public static void main(final String... args) throws IOException {
        final var config = ConfigHelper.saveAndLoad("config.json", JsonConfig.class);
        final var mongo = new MongoDatabaseManager(config.mongo.connection);

        final var cronScheduler = new CronScheduler(4);
        cronScheduler.start(config.cron.blockchain, new UpdateBlockchain(mongo, config));
        cronScheduler.start(config.cron.walletBalance, new UpdateWalletBalance(mongo, config));
        cronScheduler.start(config.cron.plots, new UpdatePlots(mongo, config));
        cronScheduler.start(config.cron.statistics, new UpdateStatistics(mongo, config));

        if (StringUtils.isNotEmpty(config.okex.apikey)) {
            cronScheduler.start(config.cron.okex, new OkexTask(mongo, config));
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cronScheduler.shutdown();
            mongo.close();
        }));
    }
}
