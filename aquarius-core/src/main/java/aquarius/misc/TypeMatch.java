package aquarius.misc;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public final class TypeMatch {
	private TypeMatch() {}

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
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
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
				} catch (Exception e) {
					throw new RuntimeException(e);
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
			if(!(t instanceof When)) {
				return false;
			}
			return this.targetClass.equals(((When<?>)t).targetClass);
		}

		@Override
		public int hashCode() {
			return this.targetClass.hashCode();
		}
	}

	public interface WhenBuilder {
		public <T> WhenBuilder when(Class<T> targetClass, WhenAction<T> action);
		public void orElse(ElseAction action);
	}

	@FunctionalInterface
	public static interface WhenAction<T> {
		public void apply(T value) throws Exception;
	}

	@FunctionalInterface
	public static interface ElseAction {
		public void apply(Optional<Object> value) throws Exception;
	}
}