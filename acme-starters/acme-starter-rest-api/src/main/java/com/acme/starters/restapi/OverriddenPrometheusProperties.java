package com.acme.starters.restapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "acme.management.prometheus.metrics.export")
public class OverriddenPrometheusProperties {

    /**
     * Whether to enable publishing exemplars as part of the scrape payload to Prometheus.
     * Turn this on only if prometheus version >= 2.43 (cf. <a href="https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.2.0-Release-Notes/55a926de3e0f1f6a55151d859efeab5819f8172f#broader-exemplar-support-in-micrometer-112">Spring Boot wiki</a>).
     */
    private boolean exemplars = false;

    public boolean isExemplars() {
        return exemplars;
    }

    public void setExemplars(boolean exemplars) {
        this.exemplars = exemplars;
    }
}
