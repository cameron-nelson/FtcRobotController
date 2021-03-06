/*
 Copyright (c) 2019 HF Robotics (http://www.hfrobots.com)
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

package com.hfrobots.tnt.corelib.metrics;

/**
 * An adapter for GaugeMetricSources that reads the real value every "n"th time.
 */

public class SamplingMetricSource implements GaugeMetricSource {
    private final GaugeMetricSource realSource;

    private final int sampleFrequency;

    private long sampleCount;

    public SamplingMetricSource(GaugeMetricSource realSource, int sampleFrequency) {
        this.realSource = realSource;
        this.sampleFrequency = sampleFrequency;
    }

    @Override
    public String getSampleName() {
        return realSource.getSampleName();
    }

    @Override
    public double getValue() {
        sampleCount++;

        // Always report the first value
        if (sampleCount == 1) {
            return realSource.getValue();
        }

        if (sampleCount % sampleFrequency == 0) {
            return realSource.getValue();
        }

        return MetricsSampler.NO_REPORT_VALUE;
    }
}
