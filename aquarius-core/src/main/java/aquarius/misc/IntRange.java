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

public class IntRange {
	private final int start;	// inclusive
	private final int stop;	// inclusive

	public IntRange(int start, int stop) {
		this.start = start;
		this.stop = stop;
	}

	public int getStart() {
		return this.start;
	}

	public int getStop() {
		return this.stop;
	}

	public boolean withinRange(int value) {
		return value >= this.start && value <= this.stop;
	}

	public String toString() {
		return (char) this.start + "..." + (char) this.stop;
	}
}
