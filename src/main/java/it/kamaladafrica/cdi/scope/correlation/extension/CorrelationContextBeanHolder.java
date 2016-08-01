package it.kamaladafrica.cdi.scope.correlation.extension;

import javax.enterprise.context.ApplicationScoped;

import org.apache.deltaspike.core.impl.scope.AbstractBeanHolder;

@ApplicationScoped
class CorrelationContextBeanHolder extends AbstractBeanHolder<CorrelationKey> {

	private static final long serialVersionUID = 6881084340214395214L;


}
