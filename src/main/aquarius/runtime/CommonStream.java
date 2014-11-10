package aquarius.runtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import aquarius.misc.Utf8Util;
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
		this(sourceName, source.getBytes(DEFAULT_CHARSET));
	}

	public CommonStream(String fileName) throws IOException {
		this(fileName, Files.readAllBytes(Paths.get(fileName)));
	}

	public CommonStream(String sourceName, InputStream inputStream, boolean close) {
		this(sourceName, readBytes(inputStream, close));
	}

	private CommonStream(String sourceName, byte[] buffer) {
		this.sourceName = sourceName;
		this.buffer = buffer;
		this.bufferSize = buffer.length;
	}

	private static byte[] readBytes(InputStream inputStream, boolean close) {
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
		return bufStream.toByteArray();
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
	public Token createToken(int startPos, int length)
			throws IndexOutOfBoundsException {
		if(!this.checkIndexRange(startPos)) {
			throw new IndexOutOfBoundsException("start position is " + startPos + 
					", but buffer size is " + this.bufferSize);
		}
		if(length < 0) {
			throw new IndexOutOfBoundsException("length is non negative: " + length);
		}
		if(startPos + length > this.getInputSize()) {
			length = this.bufferSize - startPos;
		}
		return new CommonToken(startPos, length);
	}

	private static class CommonToken implements Token {
		private final int startPos;
		private final int length;

		private CommonToken(int startPos, int length) {
			this.startPos = startPos;
			this.length = length;
		}

		@Override
		public int getStartPos() {
			return this.startPos;
		}

		@Override
		public int getSize() {
			return this.length;
		}

		@Override
		public String getText(AquariusInputStream srcInput) {
			CommonStream stream = (CommonStream) srcInput;
			return new String(stream.buffer, this.startPos, this.getSize(), DEFAULT_CHARSET);
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
		public int getPosInLine(AquariusInputStream srcInput) {
			CommonStream stream = (CommonStream) srcInput;
			int latestNewLinePos = 0;
			for(int i = 0; i < stream.bufferSize; i++) {
				if(stream.buffer[i] == '\n') {
					latestNewLinePos = i;
				}
				if(i == this.getStartPos()) {
					break;
				}
			}
			int count = 0;
			int pos = latestNewLinePos;
			while(pos < this.getStartPos()) {
				pos = Utf8Util.getUtf8NextPos(pos, stream.buffer[pos]);
				count++;
			}
			return count;
		}

		@Override
		public String toString() {
			return "token<" + this.startPos + "-" + this.length + ">";
		}

		@Override
		public boolean equals(Object target) {
			if(target instanceof Token) {
				Token token = (Token) target;
				return this.getStartPos() == token.getStartPos() && 
						this.getSize() == token.getSize();
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
