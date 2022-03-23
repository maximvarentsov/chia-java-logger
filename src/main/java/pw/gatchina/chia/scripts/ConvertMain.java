package pw.gatchina.chia.scripts;

import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import pw.gatchina.chia.JsonConfig;
import pw.gatchina.chia.MongoDatabaseManager;
import pw.gatchina.chia.models.InstrumentSwap;
import pw.gatchina.util.ConfigHelper;

import java.util.ArrayList;

/**
 * @author maximvarentsov
 * @since 07.08.2021
 */
public class ConvertMain {

    public static void main(String[] args) throws Exception {
        final var config = ConfigHelper.saveAndLoad("config.json", JsonConfig.class);
        final var mongo = new MongoDatabaseManager(config.mongo.connection);

        convertInstruments(mongo);
    }

    public static void convertInstruments(final @NotNull MongoDatabaseManager mongo) {
        final var instruments = mongo
                .getCollection("instruments", Document.class)
                .find().into(new ArrayList<>());

        final var instrumentsSwapList = new ArrayList<InstrumentSwap>();

        for (final var instrument : instruments) {
            final var id = instrument.getObjectId("_id");
            final var name = instrument.getString("instrument");
            final var value = instrument.getDouble("value");

            final var instrumentSwap = new InstrumentSwap();
            instrumentSwap.id = id;
            instrumentSwap.name = name;
            instrumentSwap.value = value;

            instrumentsSwapList.add(instrumentSwap);
        }

        if (instrumentsSwapList.isEmpty()) {
            return;
        }

        mongo.getCollection("instruments_swap", InstrumentSwap.class)
                .insertMany(instrumentsSwapList);
    }
}
