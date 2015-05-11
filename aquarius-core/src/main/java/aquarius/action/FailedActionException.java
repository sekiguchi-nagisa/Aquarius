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

public class FailedActionException extends RuntimeException {
    private static final long serialVersionUID = -8936691216403555784L;

    public FailedActionException() {
        super();
    }

    public FailedActionException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;    // not create stack trace
    }
}
