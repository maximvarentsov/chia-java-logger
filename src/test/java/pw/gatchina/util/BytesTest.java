package pw.gatchina.util;

/*
 * @(#)BytesTest.java  1.0 Sept 30, 2008
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

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;

// 1 YB[yottabyte(s)] =
// 1208925819614629174706176 B[byte(s)]
// 1180591620717411303424 KB[kilobyte(s)]
// 1152921504606846976 MB[megabyte(s)]
// 1125899906842624 GB[gigabyte(s)]
// 1099511627776 TB[terabyte(s)]
// 1073741824 PB[petabyte(s)]
// 1048576 EB[exabyte(s)]
// 1024 ZB[zettabyte(s)]
// 1 YB[yottabyte(s)]

public class BytesTest {
    @Test
    public void YBtoB() {
        assertEquals("YBtoB", "1208925819614629174706176", convertFrom(Bytes.YB, "1", Bytes.B));
    }

    @Test
    public void YBtoYB() {
        assertEquals("YBtoYB", "1", convertFrom(Bytes.YB, "1", Bytes.YB));
    }

    @Test
    public void BtoB() {
        assertEquals("BtoB", "1", convertFrom(Bytes.B, "1", Bytes.B));
    }

    @Test
    public void BtoYB() {
        assertEquals("BtoYB", "0", convertFrom(Bytes.B, "1", Bytes.YB));
    }

    @Test
    public void roundUp() {
        assertEquals("roundUp", "5 KB", Bytes.friendly(Bytes.B, new BigInteger("5080")));
    }

    @Test
    public void roundDown() {
        assertEquals("roundDown", "4 KB", Bytes.friendly(Bytes.B, new BigInteger("4588")));
    }

    private String convertFrom(Bytes sourceType, String sourceAmt, Bytes destType) {
        return destType.convertFrom((new BigInteger(sourceAmt)), sourceType).toString();
    }
}
