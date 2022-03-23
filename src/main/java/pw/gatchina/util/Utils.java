package pw.gatchina.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class Utils {

    public static double toPetabytes(final long bytes) {
        return bytes / Math.pow(1024, 5);
    }

    public static double toPetabytes(final @NotNull BigInteger bytes) {
        return bytes.divide(BigInteger.valueOf(1024).pow(5)).doubleValue();
    }

    /**
     * Calculated by formula from chia FAQ
     * https://github.com/Chia-Network/chia-blockchain/wiki/FAQ#i-have-only-10-tb-will-i-ever-win-xch-on-mainnet
     *
     * @param totalPlotSize summary bytes size of farmed plots
     * @param networkSpace chia total network space
     * @return chance to win
     */
    public static double chanceToWin(final long totalPlotSize, final @NotNull BigInteger networkSpace) {
        final var chancePerDay = 4608;
        final var coinsPerBlock = 2;
        return Utils.toPetabytes(totalPlotSize) / Utils.toPetabytes(networkSpace) * chancePerDay * coinsPerBlock;
    }

   /**
    * The rest of the codebase uses mojos everywhere. Only uses these units for user facing interfaces
    * 1 chia (XCH) is 1,000,000,000,000 mojo (1 Trillion)
    *
    * https://github.com/Chia-Network/chia-blockchain/blob/9cc908678b1255c9a520c322302ba35084676e08/chia/cmds/units.py#L6
    */
    public static double mojoToChia(long mojo) {
        return mojo / 1.0E12;
    }
}
