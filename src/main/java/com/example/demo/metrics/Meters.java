package com.example.demo.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Meters {

    public static final String VARIANT = "variant";
    public static final String NO_VARIANTS_WARNING = "No Variants found for experiment with name: {}";
    private static final String CIRCUITBREAKER_PREFIX = "circuitbreaker";

    private final MetricRegistry metricRegistry;
    private final Logger log = LoggerFactory.getLogger(Meters.class);

    public Meters(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
        checkRegistryForStandardMetrics(metricRegistry);
    }

    private void checkRegistryForStandardMetrics(MetricRegistry metricRegistry) {
        registerMetricSet("jvm.gc", new GarbageCollectorMetricSet(), metricRegistry);
        registerMetricSet("jvm.memory", new MemoryUsageGaugeSet(), metricRegistry);
        registerMetricSet("jvm.threads", new ThreadStatesGaugeSet(), metricRegistry);
        registerMetricSet("jvm.process.mem", new ProcessMemoryUsageGaugeSet(), metricRegistry);
        registerMetricSet("jvm.lang", new ClassCompilationGaugeSet(), metricRegistry);
        metricRegistry.gauge("jvm.fd.count", FileDescriptorCountGauge::new);
    }

    private void registerMetricSet(String prefix, MetricSet metricSet, MetricRegistry registry) {
        for (Entry<String, Metric> entry : metricSet.getMetrics().entrySet()) {
            String metricName = prefix + "." + entry.getKey();
            Metric metric = entry.getValue();
            if (!registry.getNames().contains(metricName)) {
                registry.register(metricName, metric);
            }
        }
    }

    public Counter getCounter(String counterName) {
        return getCounter(counterName, null);
    }

    public Counter getCounter(String counterName, Map<String, String> tags) {
        String formattedMetricAndTags = formatMetricAndTags(counterName, tags);
        return metricRegistry.counter(formattedMetricAndTags);
    }

    public Meter getMeter(String meterName) {
        return getMeter(meterName, null);
    }

    public Meter getMeter(String meterName, Map<String, String> tags) {
        String formattedMeterName = formatMetricAndTags(meterName, tags);
        return metricRegistry.meter(formattedMeterName);
    }


    public Timer getTimer(String timerName) {
        return getTimer(timerName, null);
    }

    public Timer getTimer(String timerName, Map<String, String> tags) {
        String formattedTimerName = formatMetricAndTags(timerName, tags);
        return metricRegistry.timer(formattedTimerName);
    }

    /**
     * @deprecated use MShellBundle instead to override all metric reservoirs
     */
    @Deprecated
    public Timer getTimer(String timerName, Map<String, String> tags, Reservoir reservoir) {
        Timer timer = new Timer(reservoir);
        return registerMetric(timerName, tags, timer);
    }

    public Histogram getHistogram(String histogramName) {
        return getHistogram(histogramName, null);
    }

    public Histogram getHistogram(String histogramName, Map<String, String> tags) {
        String formattedHistogramName = formatMetricAndTags(histogramName, tags);
        return metricRegistry.histogram(formattedHistogramName);
    }

    public Histogram getHistogram(String histogramName, Map<String, String> tags, MetricRegistry.MetricSupplier<Histogram> supplier) {
        String formattedHistogramName = formatMetricAndTags(histogramName, tags);
        return metricRegistry.histogram(formattedHistogramName, supplier);
    }

    public <T extends Metric> T registerMetric(String metricName, T metric) {
        return registerMetric(metricName, null, metric);
    }

    public <T extends Metric> T registerMetric(String metricName, Map<String, String> tags, T metric) {
        String formattedMetricName = formatMetricAndTags(metricName, tags);
        if (!metricRegistry.getNames().contains(formattedMetricName)) {
            return metricRegistry.register(formattedMetricName, metric);
        } else {
            return (T) metricRegistry.getMetrics().get(formattedMetricName);
        }
    }

    /**
     * Create onClose, onOpen, and onHalfOpen gauges for the Circuit Breaker.
     * <p>
     * WARNING: A circuitbreaker name can only be registered once in the application lifecycle.
     * Otherwise a IllegalArgumentException will be thrown!
     *
     * @param name           A new unique name per Circuit Breaker
     * @param circuitBreaker The Circuit Breaker to register metrics for
     * @throws IllegalArgumentException If a Circuit Breaker name is reused.
     */
    public void registerCircuitbreaker(String name, CircuitBreaker circuitBreaker) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Circuitberaker metric name cannot be null or empty!");
        } else {
            registerMetric(CIRCUITBREAKER_PREFIX + "." + name + ".closed", (Gauge<Boolean>) circuitBreaker::isClosed);
            registerMetric(CIRCUITBREAKER_PREFIX + "." + name + ".open", (Gauge<Boolean>) circuitBreaker::isOpen);
            registerMetric(CIRCUITBREAKER_PREFIX + "." + name + ".half-open", (Gauge<Boolean>) circuitBreaker::isHalfOpen);
        }
    }

    String formatMetricAndTags(String metricName, Map<String, String> tags) {
        return tags == null ? metricName : metricName + "?" + convertTagsToString(tags);
    }

    private String convertTagsToString(Map<String, String> tags) {
        return tags.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining("&"));
    }
}