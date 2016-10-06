package com.xyz;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

/**
 * Provides information about the JVM's state.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class VirtualMachineMonitor {

	private final ThreadMXBean threadBean;
	private final MemoryMXBean memoryBean;
	private final List<GarbageCollectorMXBean> gcBeans;

	private DeltaInfo oldInfo;

	public VirtualMachineMonitor() {
		threadBean = ManagementFactory.getThreadMXBean();
		gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
		memoryBean = ManagementFactory.getMemoryMXBean();
	}

	public static class Info {

		/**
		 * time since last sample [ms]
		 */
		private final long deltaTime;

		/**
		 * thread count
		 */
		private final int threadCount;

		/**
		 * if true one or more threads are in deadlock
		 */
		private final boolean deadlock;

		/**
		 * number of threads started since last sample
		 */
		private final long deltaStarted;

		/**
		 * number of garbage collections since last sample
		 */
		private final long deltaGcCount;

		/**
		 * time spent with garbage collection since last sample [ms]
		 */
		private final long deltaGcTime;

		/**
		 * heap memory (Eden + Survivor + Tenured) [bytes]
		 */
		private final long heapMem;

		/**
		 * non heap memory (Code Cache + Permanent Generation) [bytes]
		 */
		private final long nonHeapMem;

		private final boolean areAssertionsEnabled;

		@SuppressWarnings("squid:S00107")
		public Info(final long deltaTime, final int threadCount, final boolean deadlock, final long deltaStarted,
				final long deltaGcCount, final long deltaGcTime, final long heapMem, final long nonHeapMem,
				final boolean areAssertionsEnabled) {
			this.deltaTime = deltaTime;
			this.threadCount = threadCount;
			this.deadlock = deadlock;
			this.deltaStarted = deltaStarted;
			this.deltaGcCount = deltaGcCount;
			this.deltaGcTime = deltaGcTime;
			this.heapMem = heapMem;
			this.nonHeapMem = nonHeapMem;
			this.areAssertionsEnabled = areAssertionsEnabled;
		}

		@Override
		public String toString() {
			final String displayHeapMem = String.format("%d bytes", heapMem);
			final String displayNonHeapMem = String.format("%d bytes", nonHeapMem);

			final StringBuilder sb = new StringBuilder();
			sb.append("Info");
			sb.append("{deltaTime=").append(deltaTime).append(" ms");
			sb.append(", threadCount=").append(threadCount);
			sb.append(", deadlock=").append(deadlock);
			sb.append(", deltaStarted=").append(deltaStarted);
			sb.append(", deltaGcCount=").append(deltaGcCount);
			sb.append(", deltaGcTime=").append(deltaGcTime).append(" ms");
			sb.append(", heapMem=").append(heapMem).append(" bytes=").append(displayHeapMem);
			sb.append(", nonHeapMem=").append(nonHeapMem).append(" bytes=").append(displayNonHeapMem);
			sb.append(", areAssertionsEnabled=").append(areAssertionsEnabled);
			sb.append('}');
			return sb.toString();
		}
	}

	/**
	 * all fields that are tracked as "delta"
	 */
	private static final class DeltaInfo {

		private final long time;
		private final long started;
		private final long gcCount;
		private final long gcTime;

		private DeltaInfo(final long time, final long started, final long gcCount, final long gcTime) {
			this.time = time;
			this.started = started;
			this.gcCount = gcCount;
			this.gcTime = gcTime;
		}

		public long getTime() {
			return time;
		}

		public long getStarted() {
			return started;
		}

		public long getGcCount() {
			return gcCount;
		}

		public long getGcTime() {
			return gcTime;
		}
	}

	private DeltaInfo getDeltaInfo() {

		final long time = System.nanoTime() / 1000000L;
		final long started = threadBean.getTotalStartedThreadCount();

		// total gc
		long gcCount = 0;
		long gcTime = 0;
		for (final GarbageCollectorMXBean gcBean : gcBeans) {
			gcCount += gcBean.getCollectionCount();
			gcTime += gcBean.getCollectionTime();
		}

		return new DeltaInfo(time, started, gcCount, gcTime);
	}

	@SuppressWarnings({ "ConstantConditions", "NestedAssignment", "AssertWithSideEffects",
			"squid:AssignmentInSubExpressionCheck" })
	private boolean areAssertionsEnabled() {
		boolean areAssertionsEnabled = false;
		assert areAssertionsEnabled = true; // Intentional side effect!!!
		return areAssertionsEnabled;
	}

	public synchronized Info getInfo() {

		// --- delta values ---

		long deltaTime = 0;
		long deltaStarted = 0;
		long deltaGcCount = 0;
		long deltaGcTime = 0;

		if (oldInfo == null) {
			oldInfo = getDeltaInfo(); // first time: no delta
		} else {
			final DeltaInfo newInfo = getDeltaInfo();
			deltaTime = newInfo.getTime() - oldInfo.getTime();
			deltaStarted = newInfo.getStarted() - oldInfo.getStarted();
			deltaGcCount = newInfo.getGcCount() - oldInfo.getGcCount();
			deltaGcTime = newInfo.getGcTime() - oldInfo.getGcTime();
			oldInfo = newInfo; // safe for next time
		}

		// --- static values ---

		// threads
		final int count = threadBean.getThreadCount();
		final boolean deadlock = threadBean.findDeadlockedThreads() != null;

		// memory
		final long heapMem = memoryBean.getHeapMemoryUsage().getUsed();
		final long nonHeapMem = memoryBean.getNonHeapMemoryUsage().getUsed();

		final boolean areAssertionsEnabled = areAssertionsEnabled();

		return new Info(deltaTime, count, deadlock, deltaStarted, deltaGcCount, deltaGcTime, heapMem, nonHeapMem,
				areAssertionsEnabled);
	}

	public static void main(String[] args) {
		System.out.println(new VirtualMachineMonitor().getInfo());
	}
}
