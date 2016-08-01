package it.kamaladafrica.cdi.scope.correlation.extension;

import static com.google.common.base.Preconditions.checkNotNull;

public final class CorrelationKey {

	private final String key;

	private CorrelationKey(String key) {
		checkNotNull(key, "Correlation key must not be null");
		this.key = key;
	}

	public String key() {
		return key;
	}

	@Override
	public String toString() {
		return "ContextKey(" + key + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CorrelationKey other = (CorrelationKey) obj;
		return key.equals(other.key);
	}

	public static CorrelationKey of(String key) {
		return new CorrelationKey(key);
	}

}
