package it.kamaladafrica.cdi.scope.correlation.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class CorrelationContextCounter {

	public static final int NOT_FOUND = -1;

	private final Map<CorrelationKey, AtomicInteger> countMap = new ConcurrentHashMap<CorrelationKey, AtomicInteger>();
	private final Semaphore semaphore = new Semaphore(1);

	public CorrelationContextCounter() {
	}

	public int incrementAndGet(CorrelationKey correlationKey) {
		try {
			semaphore.acquireUninterruptibly();
			AtomicInteger count = countMap.get(correlationKey);
			if (count == null) {
				count = new AtomicInteger(1);
				countMap.put(correlationKey, count);
				return count.get();
			}
			return count.incrementAndGet();
		} finally {
			semaphore.release();
		}
	}

	public int decrementAndGet(CorrelationKey correlationKey) {
		try {
			semaphore.acquireUninterruptibly();
			AtomicInteger count = countMap.get(correlationKey);
			if (count == null) {
				return NOT_FOUND;
			}
			int val = count.decrementAndGet();
			if (val == 0) {
				countMap.remove(correlationKey);
			}
			return val;
		} finally {
			semaphore.release();
		}
	}

}
