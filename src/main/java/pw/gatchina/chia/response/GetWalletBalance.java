package pw.gatchina.chia.response;

import com.google.gson.annotations.SerializedName;

public class GetWalletBalance {
    public boolean success;

    @SerializedName("wallet_balance")
    public WalletBalance walletBalance;

    public static class WalletBalance {
        @SerializedName("confirmed_wallet_balance")
        public int confirmedWalletBalance;

        @SerializedName("max_send_amount")
        public int maxSendAmount;

        @SerializedName("pending_change")
        public int pendingChange;

        @SerializedName("spendable_balance")
        public int spendableBalance;

        @SerializedName("unconfirmed_wallet_balance")
        public int unconfirmedWalletBalance;

        @SerializedName("wallet_id")
        public int walletId;
    }
}