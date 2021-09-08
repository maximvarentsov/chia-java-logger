package pw.gatchina.chia;

import org.jetbrains.annotations.NotNull;
import pw.gatchina.chia.response.GetBlockchainState;
import pw.gatchina.chia.response.GetPlots;
import pw.gatchina.chia.response.GetWalletBalance;

import java.util.Map;

public class ApiClient {
    public static String ROOT_CA = System.getProperty("user.home") + "/.chia/mainnet/config/ssl/ca/private_ca.crt";
    public static String HARVESTER_KEY = System.getProperty("user.home") + "/.chia/mainnet/config/ssl/harvester/private_harvester.key.pkcs8";
    public static String HARVESTER_CRT = System.getProperty("user.home") + "/.chia/mainnet/config/ssl/harvester/private_harvester.crt";
    public static String FULL_NODE_KEY = System.getProperty("user.home") + "/.chia/mainnet/config/ssl/full_node/private_full_node.key.pkcs8";
    public static String FULL_NODE_CRT = System.getProperty("user.home") + "/.chia/mainnet/config/ssl/full_node/private_full_node.crt";
    public static String WALLET_KEY = System.getProperty("user.home") + "/.chia/mainnet/config/ssl/wallet/private_wallet.key.pkcs8";
    public static String WALLET_CRT = System.getProperty("user.home") + "/.chia/mainnet/config/ssl/wallet/private_wallet.crt";

    public static GetWalletBalance getWalletBalance(final @NotNull String host, final int walledId) {
        final var url = host + "get_wallet_balance";
        final var params =  Map.of("wallet_id", walledId);
        return new ApiCaller(ROOT_CA, WALLET_KEY, WALLET_CRT).callSync(GetWalletBalance.class, url, params);
    }

    public static GetBlockchainState getBlockchainState(final @NotNull String host) {
        final var url = host + "get_blockchain_state";
        return new ApiCaller(ROOT_CA, FULL_NODE_KEY, FULL_NODE_CRT).callSync(GetBlockchainState.class, url);
    }

    public static GetPlots getPlots(final @NotNull String host) {
        final var url = host + "get_plots";
        return new ApiCaller(ROOT_CA, HARVESTER_KEY, HARVESTER_CRT).callSync(GetPlots.class, url);
    }
}
