package it.kamaladafrica.cdi.scope.correlation.extension;

import it.kamaladafrica.cdi.scope.correlation.CorrelationContext;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Extension;

import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;

public class CorrelationScopeExtension implements Extension {

	private CorrelationContextImpl context;

	void afterBeanDiscovery(@Observes AfterBeanDiscovery bd, BeanManager bm) {
		context = new CorrelationContextImpl(bm);
		bd.addContext(context);

		addInjectableCorrelationContext(bd, bm, context);
	}

	private void addInjectableCorrelationContext(AfterBeanDiscovery afd, BeanManager beanManager,
			final CorrelationContext context) {
		BeanBuilder<CorrelationContext> builder = new BeanBuilder<CorrelationContext>(beanManager)
				.beanClass(CorrelationContext.class).types(CorrelationContext.class)
				.beanLifecycle(new ContextualLifecycle<CorrelationContext>() {

					@Override
					public CorrelationContext create(Bean<CorrelationContext> bean,
							CreationalContext<CorrelationContext> creationalContext) {
						return new InjectableCorrelationContext(context);
					}

					@Override
					public void destroy(Bean<CorrelationContext> bean, CorrelationContext instance,
							CreationalContext<CorrelationContext> creationalContext) {
					}
				});
		afd.addBean(builder.create());
	}

	/**
	 * We can only initialize our contexts in AfterDeploymentValidation because
	 * getBeans must not be invoked earlier than this phase to reduce randomness
	 * caused by Beans no being fully registered yet.
	 */
	public void initializeContext(@Observes AfterDeploymentValidation adv, BeanManager beanManager) {
		context.init(CDI.current().select(CorrelationContextBeanHolder.class).get());
	}

	public CorrelationContextImpl getCorrelationContext() {
		return context;
	}

}
