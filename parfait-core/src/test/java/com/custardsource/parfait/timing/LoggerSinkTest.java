package com.custardsource.parfait.timing;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import junit.framework.TestCase;


public class LoggerSinkTest extends TestCase {
    public void testShouldProduceExpectedMetricString() {
        MetricMeasurement measurement = getUnitMeasurement(SI.HERTZ, 1);
        String result = new LoggerSink().buildSingleMetricResult(measurement);
        assertEquals("dummy: own 1 Hz, total 1 Hz", result);
    }

    public void testShouldNormalizeMetricStringToCorrectUnit() {
        MetricMeasurement measurement = getUnitMeasurement(SI.HERTZ, 1000000001);
        LoggerSink sink = new LoggerSink();
        sink.normalizeUnits(SI.HERTZ, SI.MEGA(SI.HERTZ));
        String result = sink.buildSingleMetricResult(measurement);
        assertEquals("dummy: own 1000.000001 MHz, total 1000.000001 MHz", result);
    }

    private MetricMeasurement getUnitMeasurement(Unit<?> unit, int amount) {
        DummyThreadMetric metric = new DummyThreadMetric(unit);
        MetricMeasurement measurement = new MetricMeasurement(metric, Thread.currentThread());
        measurement.startTimer();
        metric.incrementValue(amount);
        measurement.stopTimer();
        return measurement;
    }

}
