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

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public final class TypeMatch {
    private TypeMatch() {
    }

    public static WhenBuilder match(Object value) {
        final Set<When<?>> whenSet = new TreeSet<>();

        return new WhenBuilder() {
            @Override
            public <T> WhenBuilder when(Class<T> targetClass, WhenAction<T> action) {
                whenSet.add(new When<>(targetClass, action));
                return this;
            }

            @Override
            public void orElse(ElseAction action) {
                for(When<?> when : whenSet) {
                    if(when.match(value)) {
                        return;
                    }
                }
                try {
                    if(action != null) {
                        action.apply(Optional.ofNullable(value));
                    }
                } catch(Exception e) {
                    Utils.propagate(e);
                }
            }
        };
    }

    public interface WhenBuilder {
        <T> WhenBuilder when(Class<T> targetClass, WhenAction<T> action);

        void orElse(ElseAction action);
    }

    @FunctionalInterface
    public interface WhenAction<T> {
        void apply(T value) throws Exception;
    }

    @FunctionalInterface
    public interface ElseAction {
        void apply(Optional<Object> value) throws Exception;
    }

    protected static class When<T> implements Comparable<When<?>> {
        final Class<T> targetClass;
        final WhenAction<T> action;

        When(Class<T> targetClass, WhenAction<T> action) {
            this.targetClass = targetClass;
            this.action = action;
        }

        @SuppressWarnings("unchecked")
        public boolean match(Object value) {
            if(this.targetClass.isInstance(value)) {
                try {
                    if(this.action != null) {
                        this.action.apply((T) value);
                    }
                } catch(Exception e) {
                    Utils.propagate(e);
                }
                return true;
            }
            return false;
        }

        @Override
        public int compareTo(When<?> o) {
            return !this.targetClass.isAssignableFrom(o.targetClass) ? -1 : 1;
        }

        @Override
        public boolean equals(Object t) {
            return (t instanceof When) && this.targetClass.equals(((When<?>) t).targetClass);
        }

        @Override
        public int hashCode() {
            return this.targetClass.hashCode();
        }
    }
}
