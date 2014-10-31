package aquarius.runtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static aquarius.misc.Utf8Util.*;

/**
 * input stream for common usage.
 * @author skgchxngsxyz-opensuse
 *
 */
public class CommonStream implements AquariusInputStream {
	private String sourceName;
	private final byte[] buffer;
	private final int bufferSize;
	private int currentPos = 0;
	
	public CommonStream(String sourceName, String source) {
		this.sourceName = sourceName;
		this.buffer = source.getBytes(DEFAULT_CHARSET);
		this.bufferSize = this.buffer.length;
	}

	public CommonStream(String sourceName, InputStream inputStream, boolean close) {
		this.sourceName = sourceName;
		ByteArrayOutputStream bufStream = new ByteArrayOutputStream();
		final int size = 512;
		byte[] buf = new byte[size];
		int readSize = 0;
		try {
			while((readSize = inputStream.read(buf, 0, size)) > -1) {
				bufStream.write(buf, 0, readSize);
			}
			if(close) {
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.buffer = bufStream.toByteArray();
		this.bufferSize = this.buffer.length;
	}

	@Override
	public int getPosition() {
		return this.currentPos;
	}

	/**
	 * 
	 * @param position
	 * @return
	 * if index out of range, return false
	 */
	private boolean checkIndexRange(int position) {
		if(position < 0 || position >= this.bufferSize) {
			return false;
		}
		return true;
	}

	@Override
	public void setPosition(int position) throws IndexOutOfBoundsException {
		if(position != this.bufferSize && !this.checkIndexRange(position)) {
			throw new IndexOutOfBoundsException("position is " + position + 
					", but buffer size is " + this.bufferSize);
		}
		this.currentPos = position;
	}

	@Override
	public int fetch() {
		int pos = this.currentPos;
		if(pos == this.bufferSize) {
			return EOF;
		}
		return toUtf8Code(this.buffer, pos, getUtf8Length(this.buffer[pos]));
	}

	@Override
	public void consume() {
		if(this.currentPos == this.bufferSize) {
			return;
		}
		int pos = this.getPosition();
		byte ch = this.buffer[pos];
		pos = getUtf8NextPos(pos, ch);
		if(pos > this.bufferSize) {
			throw new RuntimeException("broken buffer");
		}
		this.currentPos = pos;
	}

	@Override
	public Token createToken(int startPos, int stopPos)
			throws IndexOutOfBoundsException {
		if(!this.checkIndexRange(startPos)) {
			throw new IndexOutOfBoundsException("start position is " + startPos + 
					", but buffer size is " + this.bufferSize);
		}
		if(startPos >= stopPos) {
			throw new IndexOutOfBoundsException("start position is " + startPos + 
					", but stop position is " + stopPos);
		}
		if(stopPos > this.getInputSize()) {
			throw new IndexOutOfBoundsException("stop position is " + stopPos + 
					", but buffer size is " + this.bufferSize);
		}
		return new CommonToken(startPos, stopPos);
	}

	private static class CommonToken implements Token {
		private final int startPos;
		private final int stopPos;

		private CommonToken(int startPos, int stopPos) {
			this.startPos = startPos;
			this.stopPos = stopPos;
		}

		@Override
		public int getStartPos() {
			return this.startPos;
		}

		@Override
		public int getStopPos() {
			return this.stopPos;
		}

		@Override
		public String getSubText(AquariusInputStream srcInput, int startOffset, int stopOffset)
				throws IndexOutOfBoundsException {
			int actualStartPos = this.getStartPos() + startOffset;
			int actualStopPos = this.getStopPos() - stopOffset;

			if(startOffset < 0 || actualStartPos >= this.getStopPos()) {
				throw new IndexOutOfBoundsException("startPos is " + this.getSize() + 
						", stopPos is " + this.getStopPos() + ", but startOffset is " + startOffset);
			}
			if(stopOffset < 0 || actualStopPos < this.getStartPos()) {
				throw new IndexOutOfBoundsException("startPos is " + this.getSize() + 
						", stopPos is " + this.getStopPos() + ", but stopOffset is " + stopOffset);
			}
			int size = actualStopPos - actualStartPos;
			if(size <= 0) {
				throw new IndexOutOfBoundsException("startPos is " + this.getSize() + 
						", stopPos is " + this.getStopPos() + ", but startOffset is " + 
						startOffset + ", stopOffset is " + stopOffset);
			}
			CommonStream stream = (CommonStream) srcInput;
			return new String(stream.buffer, actualStartPos, size, DEFAULT_CHARSET);
		}

		@Override
		public int getLineNumber(AquariusInputStream srcInput) {
			CommonStream stream = (CommonStream) srcInput;
			int lineNum = 1;
			for(int i = 0; i < stream.bufferSize; i++) {
				if(stream.buffer[i] == '\n') {
					lineNum++;
				}
				if(i == this.getStartPos()) {
					break;
				}
			}
			return lineNum;
		}

		@Override
		public String toString() {
			return "token<" + this.startPos + "-" + this.stopPos + ">";
		}

		@Override
		public boolean equals(Object target) {
			if(target instanceof Token) {
				Token token = (Token) target;
				return this.getStartPos() == token.getStartPos() && 
						this.getStopPos() == token.getStopPos();
			}
			return false;
		}
	}

	@Override
	public String getSourceName() {
		return this.sourceName;
	}

	@Override
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Override
	public int getInputSize() {
		return this.bufferSize;
	}
}
