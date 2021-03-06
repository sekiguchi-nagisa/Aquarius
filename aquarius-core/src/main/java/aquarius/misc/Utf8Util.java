/*
 * Copyright (C) 2014-2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aquarius.misc;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * helper utilities for utf8
 *
 * @author skgchxngsxyz-opensuse
 */
public final class Utf8Util {
    public final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    /**
     * for utf8. drop support 5-6 byte character
     */
    private final static byte[] utf8SkipData = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1
    };

    private Utf8Util() {
    }

    /**
     * get next character position
     *
     * @param pos current position of buffer
     * @param ch  character of current position
     * @return next utf8 character position
     */
    public static int getUtf8NextPos(int pos, byte ch) {
        return pos + utf8SkipData[Byte.toUnsignedInt(ch)];
    }

    /**
     * get length of utf8 character
     *
     * @param ch start character.
     * @return
     */
    public static int getUtf8Length(byte ch) {
        return utf8SkipData[Byte.toUnsignedInt(ch)];
    }

    /**
     * convert java character literal to utf8 code
     *
     * @param utf16 java character literal
     * @return utf8 code
     */
    public static int toUtf8Code(char utf16) {
        byte[] buf = Character.toString(utf16).getBytes(DEFAULT_CHARSET);
        return toUtf8Code(buf, 0, buf.length);
    }

    /**
     * convert unicode code point to utf8 code
     *
     * @param codePoint
     * @return utf8 code
     */
    public static int toUtf8Code(int codePoint) {
        byte[] buf = new String(Character.toChars(codePoint)).getBytes(DEFAULT_CHARSET);
        return toUtf8Code(buf, 0, buf.length);
    }

    /**
     * convert byte array to utf8 code
     *
     * @param buf
     * @param startIndex
     * @param charLength 1, 2, 3 or 4
     * @return utf8 code
     */
    public static int toUtf8Code(byte[] buf, int startIndex, int charLength) {
        switch(charLength) {
        case 1:
            return buf[startIndex];
        case 2:
            return (Byte.toUnsignedInt(buf[startIndex]) << 8) |
                    Byte.toUnsignedInt(buf[++startIndex]);
        case 3:
            return (Byte.toUnsignedInt(buf[startIndex]) << 16) |
                    (Byte.toUnsignedInt(buf[++startIndex]) << 8) |
                    Byte.toUnsignedInt(buf[++startIndex]);
        case 4:
            return (Byte.toUnsignedInt(buf[startIndex]) << 24) |
                    (Byte.toUnsignedInt(buf[++startIndex]) << 16) |
                    (Byte.toUnsignedInt(buf[++startIndex]) << 8) |
                    Byte.toUnsignedInt(buf[++startIndex]);
        default:
            throw new RuntimeException("broken string");
        }
    }

    /**
     * convert string to utf8 codes
     *
     * @param str not null
     * @return
     */
    public static int[] toUtf8Codes(String str) {
        byte[] buf = str.getBytes(DEFAULT_CHARSET);
        int startIndex = 0;
        List<Integer> utf8CodeList = new ArrayList<>();

        while(startIndex < buf.length) {
            byte b = buf[startIndex];
            int charLength = getUtf8Length(b);
            utf8CodeList.add(toUtf8Code(buf, startIndex, charLength));
            startIndex += charLength;
        }

        final int size = utf8CodeList.size();
        int[] utf8Codes = new int[size];
        for(int i = 0; i < size; i++) {
            utf8Codes[i] = utf8CodeList.get(i);
        }
        return utf8Codes;
    }

    /**
     * convert utf8 code to string
     *
     * @param utf8Code
     * @return
     */
    public static String codeToString(int utf8Code) {
        return codesToString(new int[]{utf8Code});
    }

    /**
     * convert utf8 codes to string
     *
     * @param utf8Codes
     * @return
     */
    public static String codesToString(int[] utf8Codes) {
        List<Integer> codeList = new ArrayList<>(utf8Codes.length);
        for(int code : utf8Codes) {
            codeList.add(code);
        }
        return codeListToString(codeList);
    }

    /**
     * convert utf8 code list to string
     *
     * @param utf8CodeList
     * @return
     */
    public static String codeListToString(List<Integer> utf8CodeList) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for(int code : utf8CodeList) {
            codeToString(buffer, code);
        }
        return new String(buffer.toByteArray(), DEFAULT_CHARSET);
    }

    /**
     * convert utf8 code to string and write buffer.
     *
     * @param buffer
     * @param utf8Code
     */
    private static void codeToString(ByteArrayOutputStream buffer, int utf8Code) {
        for(int i = 3; i > -1; i--) {
            int shift = i * 8;
            int mask = 0xff << shift;
            byte result = (byte) ((utf8Code & mask) >> shift);
            if(result != 0) {
                buffer.write(result);
            }
        }
    }

    public static boolean isAsciiCode(int utf8Code) {
        return utf8Code >= 0 && utf8Code <= 127;
    }

    public static boolean isUtf8Code(int code) {
        // 1 byte code
        if(isAsciiCode(code)) {
            return true;
        }
        // 2 byte code
        if(code >= 0x0080 && code <= 0x07ff) {
            return true;
        }
        // 3 byte code
        if(code >= 0x0800 && code <= 0xffff) {
            return true;
        }
        // 4 byte code
        return code >= 0x10000 && code <= 0x1fffff;
    }
}
