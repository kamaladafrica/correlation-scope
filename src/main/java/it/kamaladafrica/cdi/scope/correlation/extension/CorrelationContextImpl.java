package it.kamaladafrica.cdi.scope.correlation.extension;

import it.kamaladafrica.cdi.scope.correlation.CorrelationContext;
import it.kamaladafrica.cdi.scope.correlation.CorrelationScoped;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.deltaspike.core.util.context.AbstractContext;
import org.apache.deltaspike.core.util.context.ContextualStorage;

@Typed()
class CorrelationContextImpl extends AbstractContext implements CorrelationContext {

	private static final long serialVersionUID = -7975129755065930807L;

	private BeanManager beanManager;

	private CorrelationContextBeanHolder correlationBeanHolder;

	private final ThreadLocal<CorrelationKey> correlationKeyHolder = new ThreadLocal<CorrelationKey>();

	private final CorrelationContextCounter keyCounter = new CorrelationContextCounter();

	public CorrelationContextImpl(BeanManager beanManager) {
		super(beanManager);
		this.beanManager = beanManager;
	}

	/**
	 * We need to pass the session scoped windowbean holder and the
	 * requestscoped windowIdHolder in a later phase because getBeans is only
	 * allowed from AfterDeploymentValidation onwards.
	 */
	public void init(CorrelationContextBeanHolder correlationBeanHolder) {
		this.correlationBeanHolder = correlationBeanHolder;
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return CorrelationScoped.class;
	}

	@Override
	public boolean isActive() {
		return getActiveCorrelationKey() != null;
	}

	@Override
	protected ContextualStorage getContextualStorage(Contextual<?> contextual, boolean createIfNotExist) {
		CorrelationKey correlationKey = getActiveCorrelationKey();
		if (correlationKey == null) {
			throw new ContextNotActiveException("correlation: no correlationKey set for the current thread yet!");
		}
		return correlationBeanHolder.getContextualStorage(beanManager, correlationKey, createIfNotExist);
	}

	@Override
	public void activate(CorrelationKey correlationKey) {
		correlationKeyHolder.set(correlationKey);
		keyCounter.incrementAndGet(correlationKey);
	}

	@Override
	public void destroy(CorrelationKey correlationKey) {
		if (correlationKey != null) {
			int count = keyCounter.decrementAndGet(correlationKey);
			if (count == 0) {
				ContextualStorage correlationStorage = correlationBeanHolder.getStorageMap().remove(correlationKey);
				if (correlationStorage != null) {
					if (correlationKey.equals(correlationKeyHolder.get())) {
						this.correlationKeyHolder.set(null);
					}
					AbstractContext.destroyAllActive(correlationStorage);
				}
			}
		}
	}

	@Override
	public CorrelationKey getActiveCorrelationKey() {
		return correlationKeyHolder.get();
	}

}
