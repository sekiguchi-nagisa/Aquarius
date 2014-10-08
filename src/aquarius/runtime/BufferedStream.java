package aquarius.runtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class BufferedStream implements AquariusInputStream {
	private String sourceName;
	private final byte[] buffer;
	private final int bufferSize;
	private int currentPos = 0;
	
	public BufferedStream(String sourceName, String source) {
		this.sourceName = sourceName;
		this.buffer = source.getBytes(Charset.forName("UTF-8"));
		this.bufferSize = this.buffer.length;
	}

	public BufferedStream(String sourceName, InputStream inputStream, boolean close) {
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
		if(!this.checkIndexRange(position)) {
			throw new IndexOutOfBoundsException("position is " + position + 
					", but buffer size is " + this.bufferSize);
		}
		this.currentPos = position;
	}

	@Override
	public int fetch() {
		if(!this.checkIndexRange(this.currentPos)) {
			return EOF;
		}
		return this.buffer[this.currentPos];
	}

	@Override
	public int consume() {
		int ch = this.fetch();
		if(ch != EOF) {
			this.currentPos++;
		}
		return ch;
	}

	@Override
	public Token createToken(int startPos, int stopPos)
			throws IndexOutOfBoundsException {
		if(!this.checkIndexRange(startPos)) {
			throw new IndexOutOfBoundsException("start position is " + startPos + 
					"but buffer size is " + this.bufferSize);
		}
		if(startPos >= stopPos) {
			throw new IndexOutOfBoundsException("start position is " + startPos + 
					", but stop position is " + stopPos);
		}
		if(this.fetch() == EOF) {
			throw new IndexOutOfBoundsException("stop position is " + startPos + 
					"but buffer size is " + this.bufferSize);
		}
		return new GeneralToken(startPos, stopPos);
	}

	class GeneralToken implements Token {
		private final int startPos;
		private final int stopPos;

		private GeneralToken(int startPos, int stopPos) {
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
		public int getSize() {
			return this.getStopPos() - this.getStartPos();
		}

		@Override
		public String getText() {
			return this.getSubText(0, 0);
		}

		@Override
		public String getSubText(int startOffset, int stopOffset)
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
			return new String(buffer, actualStartPos, size);
		}

		@Override
		public int getLineNumber() {
			int lineNum = 1;
			for(int i = 0; i < bufferSize; i++) {
				if(buffer[i] == '\n') {
					lineNum++;
				}
				if(i == this.getStartPos()) {
					break;
				}
			}
			return lineNum;
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
