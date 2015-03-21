/*
 * Copyright (C) 2015 Nagisa Sekiguchi
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

public class Tuple2<A, B> {
	private final A a;
	private final B b;

	public Tuple2(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A get0() {
		return this.a;
	}

	public B get1() {
		return this.b;
	}

	protected static String stringify(Object... values) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('(');
		for(int i = 0; i < values.length; i++) {
			if(i > 0) {
				sBuilder.append(", ");
			}
			sBuilder.append(values[i]);
		}
		sBuilder.append(')');
		return sBuilder.toString();
	}

	@Override
	public String toString() {
		return stringify(this.get0(), this.get1());
	}
}
