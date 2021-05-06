package pw.gatchina.chia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.gatchina.chia.tasks.UpdateBlockchain;
import pw.gatchina.chia.tasks.UpdatePlots;
import pw.gatchina.chia.tasks.UpdateWalletBalance;
import pw.gatchina.util.ConfigHelper;
import pw.gatchina.util.CronScheduler;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String... args) throws IOException {
        final var config = ConfigHelper.saveAndLoad("config.json", JsonConfig.class);
        final var mongo = new MongoDatabaseManager(config.mongo.connection);

        final var cronScheduler = new CronScheduler(3);
        cronScheduler.start(config.cron.blockchain, new UpdateBlockchain(mongo, config));
        cronScheduler.start(config.cron.walletBalance, new UpdateWalletBalance(mongo, config));
        cronScheduler.start(config.cron.plots, new UpdatePlots(mongo, config));
    }
}
