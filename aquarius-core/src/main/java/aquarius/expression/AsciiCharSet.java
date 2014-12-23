package aquarius.expression;

import java.util.Arrays;

import aquarius.AquariusInputStream;
import aquarius.ParserContext;
import aquarius.misc.IntRange;
import aquarius.misc.Utf8Util;

public class AsciiCharSet extends CharSet {
	private final byte[] asciiMap;

	private void updateAsciiMap(int[] chars) {
		for(int ch: chars) {
			this.updateAsciiMap(ch);
		}
	}

	private void updateAsciiMap(int start, int stop) {
		int size = stop - start + 1;
		int[] codes = new int[size];
		for(int i = 0; i < size; i++) {
			codes[i] = start + i;
		}
		this.updateAsciiMap(codes);
	}

	private void updateAsciiMap(int ch) {
		this.asciiMap[ch] = 1;
	}

	public AsciiCharSet(int[] chars) {
		super(chars);
		this.asciiMap = new byte[128];
		Arrays.fill(this.asciiMap, (byte)0);
		this.updateAsciiMap(chars);
	}

	@Override
	public CharSet r(int start, int stop) throws IllegalArgumentException {
		super.r(start, stop);
		if(Utf8Util.isAsciiCode(start) && Utf8Util.isAsciiCode(stop)) {
			this.updateAsciiMap(start, stop);
			return this;
		}
		CharSet newCharSet = new CharSet(this.getChars());
		for(IntRange range: this.rangeList) {
			newCharSet.r(range.getStart(), range.getStop());
		}
		return newCharSet;
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		final int fetchedCh = input.fetchByte();

		if(Utf8Util.isAsciiCode(fetchedCh)) {
			if(this.asciiMap[fetchedCh] == 1) {
				input.consumeByte();
				return true;
			}
		}
		context.pushFailure(input.getPosition(), this);
		return false;
	}
}
