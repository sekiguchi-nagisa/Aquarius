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

package aquarius.example;

import aquarius.Parser;
import aquarius.Rule;
import aquarius.Token;
import aquarius.Grammar;

import static aquarius.Expressions.*;

@Grammar
public interface SampleParser extends Parser {
    default Rule<Token> Expr() {
        return () ->
                seq(__, Add(), __).map((ctx, a) -> a.get1());
    }

    default Rule<Token> Add() {
        return () ->
                $(Mul(), __, str("+"), __, Add())
                        .or($(Mul(), __, str("-"), __, Add()))
                        .or(Mul());
    }

    default Rule<Token> Mul() {
        return () ->
                $(Primary(), __, str("*"), __, Mul())
                        .or($(Primary(), __, str("/"), __, Mul()))
                        .or(Primary());
    }

    default Rule<Token> Primary() {
        return () ->
                $(str("("), __, Add(), __, str(")"))
                        .or(Num());
    }

    default Rule<Token> Num() {
        return () ->
                $("0")
                        .or($(ch('-', '+').opt(), r('1', '9'), r('0', '9').zeroMore()));
    }
}
