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

public class Tuple5<A, B, C, D, E> extends Tuple4<A, B, C, D> {
	private final E e;

	public Tuple5(A a, B b, C c, D d, E e) {
		super(a, b, c, d);
		this.e = e;
	}

	public E get4() {
		return this.e;
	}

	@Override
	public String toString() {
		return stringify(this.get0(), 
			this.get1(), this.get2(), this.get3(), this.get4());
	}
}
