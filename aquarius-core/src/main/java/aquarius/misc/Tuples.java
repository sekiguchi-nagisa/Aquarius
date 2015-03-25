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

/**
 * helper methods for tuple
 * @author skgchxngsxyz-opensuse
 *
 */
public final class Tuples {
	private Tuples(){}

	public final static <A, B>Tuple2<A, B> of(A a, B b) {
		return new Tuple2<>(a, b);
	}

	public final static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) {
		return new Tuple3<>(a, b, c);
	}

	public final static <A, B, C, D> Tuple4<A, B, C, D> of(A a, B b, C c, D d) {
		return new Tuple4<>(a, b, c, d);
	}

	public final static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
		return new Tuple5<>(a, b, c, d, e);
	}
}
