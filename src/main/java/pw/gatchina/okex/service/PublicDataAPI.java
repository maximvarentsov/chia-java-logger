package pw.gatchina.okex.service;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author maximvarentsov
 * @since 07.08.2021
 */
public interface PublicDataAPI {
    /**
     * Retrieve a list of instruments with open contracts.
     *
     * https://www.okex.com/docs-v5/en/#rest-api-public-data-get-instruments
     *
     * @param instType Instrument type: SPOT MARGIN SWAP FUTURES  OPTION
     * @param uly Underlying. Only applicable to FUTURES/SWAP/OPTION. Required for OPTION.
     * @param instId Instrument ID
     */
    @GET("/api/v5/public/instruments")
    Call<String> getInstrument(@Query("instType") String instType, @Query("uly") String uly, @Query("instId") String instId);

    /**
     * Retrieve API server time.
     *
     * @return System time, Unix timestamp format in milliseconds, e.g. 1597026383085
     */
    @GET("/api/v5/public/time")
    Call<String> getTime();

    /**
     * Retrieve funding rate.
     * https://www.okex.com/docs-v5/en/#rest-api-public-data-get-funding-rate
     *
     * @param instId Instrument ID, e.g. BTC-USD-SWAP only applicable to SWAP
     */
    @GET("/api/v5/public/funding-rate")
    Call<String> getFundingRate(@Query("instId") String instId);

    /**
     * Retrieve funding rate history. This endpoint can retrieve data from the last 3 months.
     * https://www.okex.com/docs-v5/en/#rest-api-public-data-get-funding-rate-history
     *
     * @param instId Instrument ID, e.g. BTC-USD-SWAP only applicable to SWAP.
     * @param after Pagination of data to return records newer than the requested ts.
     * @param before Pagination of data to return records earlier than the requested ts.
     * @param limit Number of results per request. The maximum is 100; The default is 100.
     */
    @GET("GET /api/v5/public/funding-rate-history")
    Call<String> getFundingRateHistory(@Query("instId") String instId, @Query("after") String after, @Query("before") String before, @Query("limit") String limit);

    /**
     * Retrieve funding rate history. This endpoint can retrieve data from the last 3 months.
     * https://www.okex.com/docs-v5/en/#rest-api-public-data-get-limit-price
     *
     * @param instId Instrument ID, e.g. BTC-USD-180216 only applicable to FUTURES/SWAP/OPTION
     */
    @GET("/api/v5/public/price-limit")
    Call<String> getPriceLimit(@Query("instId") String instId);

    /**
     * Retrieve mark price.
     *
     * We set the mark price based on the SPOT index and at a reasonable basis to prevent individual users from
     * manipulating the market and causing the contract price to fluctuate.
     *
     * https://www.okex.com/docs-v5/en/#rest-api-public-data-get-mark-price
     * @param instType MARGIN SWAP FUTURES OPTION
     * @param uly Underlying
     * @param instId Instrument ID, e.g. BTC-USD-SWAP
     */
    @GET("/api/v5/public/mark-price")
    Call<MarkPrice> getMarkPrice(@Query("instType") String instType, @Query("uly") String uly, @Query("instId") String instId);

    /**
     * Get Underlying
     * Rate Limit: 20 requests per 2 seconds
     *
     * https://www.okex.com/docs-v5/en/#rest-api-public-data-get-underlying
     * @param instType SWAP FUTURES OPTION
     */
    @GET("/api/v5/public/underlying")
    Call<String> getUnderlying(final @NotNull @Query("instType") String instType);

}