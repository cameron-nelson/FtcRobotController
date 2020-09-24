package com.hfrobots.tnt.corelib.metrics;

import com.hfrobots.tnt.corelib.control.NinjaGamePad;

public interface MetricsSampler {

    double NO_REPORT_VALUE = Double.MIN_VALUE;

    void doSamples();

    void addSource(GaugeMetricSource metricSource);

    void addGamepad(String name, NinjaGamePad gamepad);
}
