package pw.gatchina.okex.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pw.gatchina.okex.APIConfiguration;
import pw.gatchina.okex.client.APIClient;

/**
 * @author maximvarentsov
 * @since 07.08.2021
 */
@SuppressWarnings("unused")
public class PublicDataAPIService {
    private final APIClient client;
    private final PublicDataAPI publicDataAPI;

    public PublicDataAPIService(final APIConfiguration config) {
        this.client = new APIClient(config);
        this.publicDataAPI = client.createService(PublicDataAPI.class);
    }

    public String getInstruments(final Instruments instrument) {
        return client.executeSync(publicDataAPI.getInstrument(instrument.toString(),null, null));
    }

    public String getInstruments(final Instruments instrument, final String uly, final String instId) {
        return client.executeSync(publicDataAPI.getInstrument(instrument.toString(), uly, instId));
    }

    public String getTime() {
        return client.executeSync(publicDataAPI.getTime());
    }

    public MarkPrice getMarkPrice(final @NotNull Instruments instrument,
                                  final @Nullable String uly,
                                  final @Nullable String instId) {
        return client.executeSync(publicDataAPI.getMarkPrice(instrument.toString(), instId, uly));
    }
    public MarkPrice getMarkPrice(final @NotNull Instruments instrument) {
        return client.executeSync(publicDataAPI.getMarkPrice(instrument.toString(), null, null));
    }

    public String getFundingRate(final String instId) {
        return client.executeSync(publicDataAPI.getFundingRate(instId));
    }

    public String getFundingRateHistory(final String instId, final String after,
                                        final String before, final String limit) {
        return client.executeSync(publicDataAPI.getFundingRateHistory(instId, after, before, limit));
    }

    public String getPriceLimit(final String instId) {
        return client.executeSync(publicDataAPI.getPriceLimit(instId));
    }

    public String getUnderlying(final @NotNull Instruments instId) {
        return client.executeSync(publicDataAPI.getUnderlying(instId.toString()));
    }

}