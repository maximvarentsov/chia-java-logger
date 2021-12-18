package pw.gatchina.okex.utils;

import org.jetbrains.annotations.Nullable;

public class StringUtils {
    public static boolean isEmpty(final @Nullable CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final @Nullable CharSequence cs) {
        return ! isEmpty(cs);
    }
}
