package com.example.demo.metrics;

import com.google.common.collect.ImmutableMap;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Meters implements MeterBinder {

    private MeterRegistry meterRegistry;

    public Counter getCounter(String counterName) {
        return getCounter(counterName, ImmutableMap.of());
    }

    public Counter getCounter(String counterName, Map<String, String> tags) {
        if (tags == null) {
            return getCounter(counterName);
        }
        return meterRegistry
                .counter(counterName,
                        tags.entrySet()
                                .stream()
                                .map(entry -> Tag.of(entry.getKey(), entry.getValue()))
                                .collect(Collectors.toList()));
    }

    @Override
    public void bindTo(@NonNull MeterRegistry registry) {
        this.meterRegistry = registry;
    }

    public DistributionSummary getSummary(String name) {
        return meterRegistry
                .summary(name, "avg", name + "_sum/" + name + "_count");
    }
//
//
//    public Timer getTimer(String timerName) {
//        return getTimer(timerName, null);
//    }
//
//    public Timer getTimer(String timerName, Map<String, String> tags) {
//        String formattedTimerName = formatMetricAndTags(timerName, tags);
//        return meterRegistry.timer(formattedTimerName);
//    }
//
//    /**
//     * @deprecated use MShellBundle instead to override all metric reservoirs
//     */
//    @Deprecated
//    public Timer getTimer(String timerName, Map<String, String> tags, Reservoir reservoir) {
//        Timer timer = new Timer(reservoir);
//        return registerMetric(timerName, tags, timer);
//    }
//
//    public Histogram getHistogram(String histogramName) {
//        return getHistogram(histogramName, null);
//    }
//
//    public Histogram getHistogram(String histogramName, Map<String, String> tags) {
//        String formattedHistogramName = formatMetricAndTags(histogramName, tags);
//        return meterRegistry.histogram(formattedHistogramName);
//    }
//
//    public Histogram getHistogram(String histogramName, Map<String, String> tags, MetricRegistry.MetricSupplier<Histogram> supplier) {
//        String formattedHistogramName = formatMetricAndTags(histogramName, tags);
//        return meterRegistry.histogram(formattedHistogramName, supplier);
//    }

}