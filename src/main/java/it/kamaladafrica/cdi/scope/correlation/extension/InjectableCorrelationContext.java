package it.kamaladafrica.cdi.scope.correlation.extension;

import it.kamaladafrica.cdi.scope.correlation.CorrelationContext;

class InjectableCorrelationContext implements CorrelationContext {

	private static final long serialVersionUID = 4220727581428479321L;

	private final CorrelationContext correlationContext;

	InjectableCorrelationContext(CorrelationContext correlationContext) {
		this.correlationContext = correlationContext;
	}

	@Override
	public void activate(CorrelationKey contextKey) {
		correlationContext.activate(contextKey);
	}

	@Override
	public void destroy(CorrelationKey contextKey) {
		correlationContext.destroy(contextKey);
	}

	@Override
	public boolean isActive() {
		return correlationContext.isActive();
	}

	@Override
	public CorrelationKey getActiveCorrelationKey() {
		return correlationContext.getActiveCorrelationKey();
	}

}
