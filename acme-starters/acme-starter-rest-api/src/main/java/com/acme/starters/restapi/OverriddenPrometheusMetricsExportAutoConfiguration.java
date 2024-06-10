package com.acme.starters.restapi;

import io.prometheus.client.CollectorRegistry;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.TextOutputFormat;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

import java.util.Set;

@AutoConfiguration(before = PrometheusMetricsExportAutoConfiguration.class)
@EnableConfigurationProperties(OverriddenPrometheusProperties.class)
@ConditionalOnEnabledMetricsExport("prometheus")
@ConditionalOnAvailableEndpoint(endpoint = PrometheusScrapeEndpoint.class)
public class OverriddenPrometheusMetricsExportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "acme.management.prometheus.metrics.export.exemplars", havingValue = "false")
    public PrometheusScrapeEndpoint prometheusEndpoint(CollectorRegistry collectorRegistry) {
        return new TextPlainOnlyPrometheusScrapeEndpoint(collectorRegistry);
    }

    /**
     * {@link PrometheusScrapeEndpoint} that doesn't expose exemplars.
     * Exemplars work since the prometheus version >= 2.43.
     */
    @WebEndpoint(id = "prometheus")
    public static class TextPlainOnlyPrometheusScrapeEndpoint extends PrometheusScrapeEndpoint {

        public TextPlainOnlyPrometheusScrapeEndpoint(CollectorRegistry collectorRegistry) {
            super(collectorRegistry);
        }

        @Override
        @ReadOperation(producesFrom = TextOutputFormat.class)
        public WebEndpointResponse<String> scrape(TextOutputFormat format, @Nullable Set<String> includedNames) {
            return super.scrape(TextOutputFormat.CONTENT_TYPE_004, includedNames);
        }
    }

}

