package pw.gatchina.chia.response;

import com.google.gson.annotations.SerializedName;

public class GetWalletBalance {
    public boolean success;

    @SerializedName("wallet_balance")
    public WalletBalance walletBalance;

    public static class WalletBalance {
        @SerializedName("wallet_id")
        public int walletId;

        @SerializedName("confirmed_wallet_balance")
        public long confirmedWalletBalance;

        @SerializedName("unconfirmed_wallet_balance")
        public long unconfirmedWalletBalance;

        @SerializedName("spendable_balance")
        public long spendableBalance;

        @SerializedName("pending_change")
        public long pendingChange;

        @SerializedName("max_send_amount")
        public long maxSendAmount;

        @SerializedName("unspent_coin_count")
        public long unspentCoinCount;

        @SerializedName("pending_coin_removal_count")
        public long pendingCoinRemovalCount;
    }
}