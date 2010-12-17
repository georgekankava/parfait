package com.custardsource.parfait;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class QuiescentRegistryListenerTest {
    private static final int QUIET_PERIOD_IN_SECONDS = 1;
    private static final int MS_PER_SECOND = 1000;


    @Test
    public void quiescentListenerShouldFireAfterAppropriateDelay() {
        final DelayedRunnableTester delayedRunnableTester = new DelayedRunnableTester();
        final QuiescentRegistryListener listener = new QuiescentRegistryListener(delayedRunnableTester, QUIET_PERIOD_IN_SECONDS*MS_PER_SECOND);

        final MonitorableRegistry monitorableRegistry = new MonitorableRegistry();
        monitorableRegistry.addRegistryListener(listener);

        final DummyMonitorable dummyMonitorable = new DummyMonitorable("foo");

        monitorableRegistry.register(dummyMonitorable);

        try {
            Thread.sleep((QUIET_PERIOD_IN_SECONDS+1) * MS_PER_SECOND);
        } catch (InterruptedException e) {
        }

        assertTrue("Delayed trigger should have fired", delayedRunnableTester.runCount() == 1);


        assertTrue(String.format("QuiescentListener should not have fired so quickly: %d", delayedRunnableTester.delayTaken()), delayedRunnableTester.delayTaken() >= QUIET_PERIOD_IN_SECONDS* MS_PER_SECOND);

        try {
            Thread.sleep((QUIET_PERIOD_IN_SECONDS + 2) * MS_PER_SECOND);
        } catch (InterruptedException e) {
        }

        assertTrue("Should not have fired more than once: " + delayedRunnableTester.runCount(), delayedRunnableTester.runCount() == 1);

    }

    private static class DelayedRunnableTester implements Runnable {
        private final long creationTimeStamp = System.currentTimeMillis();

        private long runInvoked = -1;
        private int runCount = 0;

        @Override
        public void run() {
            runInvoked = System.currentTimeMillis();
            runCount ++;
        }

        public int runCount() {
            return runCount;
        }

        public long delayTaken(){
            return runInvoked - creationTimeStamp;
        }
    }
}