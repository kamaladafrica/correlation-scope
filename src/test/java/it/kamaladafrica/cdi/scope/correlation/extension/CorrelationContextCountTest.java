package it.kamaladafrica.cdi.scope.correlation.extension;

import static it.kamaladafrica.cdi.scope.correlation.extension.CorrelationContextCounter.NOT_FOUND;

import org.junit.Assert;
import org.junit.Test;

public class CorrelationContextCountTest {

	@Test
	public void testCounter() {
		final CorrelationContextCounter counter = new CorrelationContextCounter();

		final CorrelationKey key1 = CorrelationKey.of("correlation key 1");
		final CorrelationKey key2 = CorrelationKey.of("correlation key 2");

		int count = counter.decrementAndGet(key1);
		Assert.assertEquals(count, NOT_FOUND);
		count = counter.decrementAndGet(key2);
		Assert.assertEquals(count, NOT_FOUND);

		count = counter.incrementAndGet(key1);
		Assert.assertEquals(1, count);
		count = counter.incrementAndGet(key2);
		Assert.assertEquals(1, count);

		count = counter.incrementAndGet(key1);
		Assert.assertEquals(2, count);
		count = counter.incrementAndGet(key2);
		Assert.assertEquals(2, count);

		count = counter.decrementAndGet(key1);
		Assert.assertEquals(1, count);
		count = counter.decrementAndGet(key2);
		Assert.assertEquals(1, count);

		count = counter.decrementAndGet(key1);
		Assert.assertEquals(0, count);
		count = counter.decrementAndGet(key2);
		Assert.assertEquals(0, count);

		count = counter.decrementAndGet(key1);
		Assert.assertEquals(NOT_FOUND, count);
		count = counter.decrementAndGet(key2);
		Assert.assertEquals(NOT_FOUND, count);
	}

}
