package aquarius.misc;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * helper utilities for utf8
 * @author skgchxngsxyz-opensuse
 *
 */
public final class Utf8Util {
	private Utf8Util() {}

	public final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	/**
	 * for utf8. drop support 5-6 byte character
	 */
	private final static byte[] utf8SkipData = {
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
		3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,1,1,1,1,1,1,1,1
	};

	/**
	 * get next character position
	 * @param pos
	 * current position of buffer
	 * @param ch
	 * character of current position
	 * @return
	 * next utf8 character position
	 */
	public final static int getUtf8NextPos(int pos, byte ch) {
		return pos + utf8SkipData[Byte.toUnsignedInt(ch)];
	}

	/**
	 * get length of utf8 character
	 * @param ch
	 * start character.
	 * @return
	 */
	public final static int getUtf8Length(byte ch) {
		return utf8SkipData[Byte.toUnsignedInt(ch)];
	}

	/**
	 * convert java character literal to utf8 code
	 * @param utf16
	 * java character literal
	 * @return
	 * utf8 code
	 */
	public final static int toUtf8Code(char utf16) {
		byte[] buf = Character.toString(utf16).getBytes(DEFAULT_CHARSET);
		return toUtf8Code(buf, 0, buf.length);
	}

	/**
	 * convert java codepoint to utf8 code
	 * @param uft16CodePoint
	 * @return
	 * utf8 code
	 */
	public final static int toUtf8Code(int uft16CodePoint) {
		byte[] buf = new String(Character.toChars(uft16CodePoint)).getBytes(DEFAULT_CHARSET);
		return toUtf8Code(buf, 0, buf.length);
	}

	/**
	 * convert byte array to utf8 code
	 * @param buf
	 * @param startIndex
	 * @param charLength
	 * 1, 2, 3 or 4
	 * @return
	 * utf8 code
	 */
	public final static int toUtf8Code(byte[] buf, int startIndex, int charLength) {
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
	 * @param str
	 * not null
	 * @return
	 */
	public final static int[] toUtf8Codes(String str) {
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
	 * @param utf8Code
	 * @return
	 */
	public final static String codeToString(int utf8Code) {
		return codesToString(new int[]{utf8Code});
	}

	/**
	 * convert utf8 codes to string
	 * @param utf8Codes
	 * @return
	 */
	public final static String codesToString(int[] utf8Codes) {
		List<Integer> codeList = new ArrayList<>(utf8Codes.length);
		for(int code: utf8Codes) {
			codeList.add(code);
		}
		return codeListToString(codeList);
	}

	/**
	 * convert utf8 code list to string
	 * @param utf8CodeList
	 * @return
	 */
	public final static String codeListToString(List<Integer> utf8CodeList) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		for(int code : utf8CodeList) {
			codeToString(buffer, code);
		}
		return new String(buffer.toByteArray(), DEFAULT_CHARSET);
	}

	/**
	 * convert utf8 code to string and write buffer.
	 * @param buffer
	 * @param utf8Code
	 */
	private final static void codeToString(ByteArrayOutputStream buffer, int utf8Code) {
		for(int i = 3; i > -1; i--) {
			int shift = i * 8;
			int mask = 0xff << shift;
			byte result = (byte) ((utf8Code & mask) >> shift);
			if(result != 0) {
				buffer.write(result);
			}
		}
	}

	public final static boolean isAsciiCode(int utf8Code) {
		return utf8Code >= 0 && utf8Code <= 127;
	}

	public final static boolean isUtf8Code(int code) {
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
