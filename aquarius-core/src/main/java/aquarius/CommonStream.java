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

package aquarius;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

import static aquarius.misc.Utf8Util.*;

/**
 * input stream implementation for common usage.
 * @author skgchxngsxyz-opensuse
 *
 */
public class CommonStream implements AquariusInputStream {
	private String sourceName;
	private final byte[] buffer;
	private final int bufferSize;
	private int currentPos = 0;
	private int[] lineNumTable;

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
		if(position < 0 || position > this.bufferSize) {
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
	public int fetchByte() {
		int pos = this.currentPos;
		if(pos == this.bufferSize) {
			return EOF;
		}
		return this.buffer[pos];
	}

	@Override
	public void consumeByte() {
		if(this.currentPos == this.bufferSize) {
			return;
		}
		this.currentPos++;
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
	public Token createToken(int startPos, int length)
			throws IndexOutOfBoundsException {
		if(!this.checkIndexRange(startPos)) {
			throw new IndexOutOfBoundsException("start position is " + startPos + 
					", but buffer size is " + this.bufferSize);
		}
		if(length < 0) {
			throw new IndexOutOfBoundsException("length is non negative: " + length);
		}
		if(startPos + length > this.bufferSize) {
			length = this.bufferSize - startPos;
		}
		return new Token(startPos, length);
	}

	public int getBufferSize() {
		return this.bufferSize;
	}

	@Override
	public String getTokenText(Token token) {
		return new String(this.buffer, token.getStartPos(), token.getSize(), DEFAULT_CHARSET);
	}

	@Override
	public int getLineNumber(Token token) {
		if(this.lineNumTable == null) {
			this.createLineNumberTable();
		}

		int index = Arrays.binarySearch(this.lineNumTable, token.getStartPos());
		if(index >= 0) {
			return index;
		} else {
			return -index - 1;
		}
	}

	private void createLineNumberTable() {
		LinkedList<Integer> newLineList = new LinkedList<>();
		newLineList.add(-1);
		for(int i = 0; i < this.bufferSize; i++) {
			if(this.buffer[i] == '\n') {
				newLineList.add(i);
			}
		}

		int index = -1;
		this.lineNumTable = new int[newLineList.size()];
		for(int newLinePos : newLineList) {
			this.lineNumTable[++index] = newLinePos;
		}
	}

	@Override
	public int getLineStartPos(Token token) {
		int latestNewLinePos = 0;
		for(int i = 0; i < this.bufferSize; i++) {
			if(this.buffer[i] == '\n') {
				latestNewLinePos = i;
			}
			if(i == token.getStartPos()) {
				break;
			}
		}
		return latestNewLinePos;
	}
}
