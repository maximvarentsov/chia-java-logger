package pw.gatchina.chia.response;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class GetWalletBalance {
    public boolean success;

    @SerializedName("wallet_balance")
    public WalletBalance walletBalance;

    public static class WalletBalance {
        @SerializedName("confirmed_wallet_balance")
        public long confirmedWalletBalance;

        @SerializedName("max_send_amount")
        public long maxSendAmount;

        @SerializedName("pending_change")
        public long pendingChange;

        @SerializedName("spendable_balance")
        public long spendableBalance;

        @SerializedName("unconfirmed_wallet_balance")
        public long unconfirmedWalletBalance;

        @SerializedName("wallet_id")
        public int walletId;
    }
}