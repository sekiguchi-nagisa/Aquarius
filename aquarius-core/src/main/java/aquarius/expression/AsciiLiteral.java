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

package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ParserContext;

/**
 * try to match ascii string literal.
 *
 * @author skgchxngsxyz-opensuse
 */
public class AsciiLiteral extends Literal {
    public AsciiLiteral(int[] targetCodes) {
        super(targetCodes);
    }

    @Override
    public boolean parseImpl(ParserContext context) {
        AquariusInputStream input = context.getInputStream();
        for(int code : this.targetCodes) {
            if(code != input.fetchByte()) {
                context.pushFailure(input.getPosition(), this);
                return false;
            }
            input.consumeByte();
        }
        return true;
    }
}
