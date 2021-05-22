package pw.gatchina.util;

/*
 * @(#)Bytes.java  2.0 Sept 30, 2008
 *
 *  The MIT License
 *
 *  Copyright (c) 2008 Malachi de AElfweald <malachid@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
//package org.eoti.util.size;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Bytes {
    B("byte"),
    KB("kilobyte"),
    MB("megabyte"),
    GB("gigabyte"),
    TB("terabyte"),
    PB("petabyte"),
    EB("exabyte"),
    ZB("zettabyte"),
    YB("yottabyte");

    Bytes(final @NotNull String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }

    public BigInteger convertFrom(final @NotNull BigInteger originalAmount, final @NotNull Bytes originalType) {
        /*
         * So, B.convertFrom(BigInteger.ONE, YB)
         * should be used to convert 1 yottabyte to bytes...
         */

        var currentAmount = originalAmount;
        var convertTo = ordinal();
        var convertFrom = originalType.ordinal();
        while (convertTo != convertFrom) {
            if (convertTo < convertFrom) {
                currentAmount = currentAmount.shiftLeft(10);
                convertFrom--;
            } else {
                currentAmount = currentAmount.shiftRight(10);
                convertFrom++;
            }
        }
        return currentAmount;
    }

    private static final List<Bytes> reversed = reverse(Bytes.values());
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final String friendlyFMT = "%s %s";

    public static String friendly(long bytes) {
        return friendly(Bytes.B, BigInteger.valueOf(bytes));
    }

    /**
     * Convert the specified amount into a human readable (though slightly less accurate)
     * result. IE:
     * '4096 B' to '4 KB'
     * '5080 B' to '5 KB' even though it is really '4 KB + 984 B'
     */
    public static String friendly(final @NotNull Bytes type, final @NotNull BigInteger value) {
        /*
         * Logic:
         * Loop from YB to B
         * If result = 0, continue
         * Else, round off
         *
         * NOTE: BigIntegers are not reusable, so not point in caching them outside the loop
         */
        for (var newType : reversed) {
            var newAmount = newType.convertFrom(value, type);
            if (newAmount.equals(BigInteger.ZERO)) {
                continue;
            }
            // Found the right one. Now to round off
            var unitBytes = Bytes.B.convertFrom(BigInteger.ONE, newType);
            var usedBytes = newAmount.multiply(unitBytes);
            var remainingBytes = Bytes.B.convertFrom(value, type).subtract(usedBytes);

            if (remainingBytes.equals(BigInteger.ZERO)) {
                return String.format(friendlyFMT, newAmount, newType);
            }

            if (remainingBytes.equals(value)) {
                return String.format(friendlyFMT, newAmount, newType);
            }

            var halfUnit = unitBytes.divide(TWO);
            if ((remainingBytes.subtract(halfUnit)).signum() < 0) {
                return String.format(friendlyFMT, newAmount, newType);
            }

            return String.format(friendlyFMT, (newAmount.add(BigInteger.ONE)), newType);
        }
        // Give up
        return String.format(friendlyFMT, value, type);
    }

    public static <T> List<T> reverse(T[] array) {
        List<T> list = Arrays.asList(array);
        Collections.reverse(list);
        return list;
    }
}
