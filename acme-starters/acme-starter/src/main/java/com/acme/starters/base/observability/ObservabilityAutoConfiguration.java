package com.acme.starters.base.observability;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.NoRepositoryBean;

@AutoConfiguration(after = MetricsAutoConfiguration.class)
@ConditionalOnBean(MetricsProperties.class)
public class ObservabilityAutoConfiguration {

    private final MetricsProperties metricsProperties;

    public ObservabilityAutoConfiguration(MetricsProperties metricsProperties) {
        this.metricsProperties = metricsProperties;
    }

    @Bean
    @ConditionalOnClass(NoRepositoryBean.class)
    public NonSpringDataRepositoryMetricsAspect nonSpringDataRepositoryMetricsAspect() {
        return new NonSpringDataRepositoryMetricsAspect(metricsProperties.getData().getRepository().getMetricName());
    }
}
