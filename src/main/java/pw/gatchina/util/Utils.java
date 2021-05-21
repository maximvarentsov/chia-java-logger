package pw.gatchina.util;

import java.text.StringCharacterIterator;

public class Utils {

    public static double toPetabytes(final long bytes) {
        //return bytes / 1024.0 / 1024.0 / 1024.0 / 1024.0/ 1024.0;
        return bytes / Math.pow(1024, 5);
    }

    /**
     * https://stackoverflow.com/a/3758880
     *
     * @param bytes the size
     * @return Formatted byte size to human readable format
     */
    public static String humanReadableByteCountBin(final long bytes) {
        final var absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        var value = absB;
        final var ci = new StringCharacterIterator("KMGTPE");
        for (var i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    /**
     * Calculated by formula from chia FAQ
     * https://github.com/Chia-Network/chia-blockchain/wiki/FAQ#i-have-only-10-tb-will-i-ever-win-xch-on-mainnet
     *
     * @param totalPlotSize summary bytes size of farmed plots
     * @param networkSpace chia total network space
     * @return chance to win
     */
    public static double chanceToWin(final long totalPlotSize, final long networkSpace) {
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
        return mojo / Math.pow(10, 12);
    }
}
