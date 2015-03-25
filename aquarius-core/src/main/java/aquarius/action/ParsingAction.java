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

package aquarius.action;

import aquarius.ParserContext;

public interface ParsingAction<R, A> {
	@FunctionalInterface
	public static interface Mapper<R, A> extends ParsingAction<R, A> {
		public R invoke(ParserContext context, A arg) throws FailedActionException, Exception;
	}

	@FunctionalInterface
	public static interface Consumer<A> extends ParsingAction<Void, A> {
		public void invoke(ParserContext context, A arg) throws FailedActionException, Exception;
	}
}
