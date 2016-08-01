package it.kamaladafrica.cdi.scope.correlation;

import it.kamaladafrica.cdi.scope.correlation.extension.CorrelationKey;

import java.io.Serializable;

public interface CorrelationContext extends Serializable {

	void activate(CorrelationKey contextKey);

	void destroy(CorrelationKey contextKey);

	boolean isActive();

	CorrelationKey getActiveCorrelationKey();
}
