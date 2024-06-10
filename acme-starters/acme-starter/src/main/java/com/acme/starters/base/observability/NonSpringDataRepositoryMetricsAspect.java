package com.acme.starters.base.observability;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener.RepositoryMethodInvocationResult.State;

import java.time.Duration;
import java.time.Instant;

/**
 * <p>{@link Aspect} that produces {@link Metrics} for non-Spring Data repositories with the same format that
 * {@link org.springframework.boot.actuate.metrics.data.MetricsRepositoryMethodInvocationListener} does for Spring Data
 * repositories.</p>
 */
@Aspect
public class NonSpringDataRepositoryMetricsAspect {

    private final String metricName;

    public NonSpringDataRepositoryMetricsAspect(String metricName) {
        this.metricName = metricName;
    }

    @Pointcut("within(org.springframework.data.repository.CrudRepository+)")
    public void crudRepository() {
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryAnnotation() {
    }

    @Around("repositoryAnnotation() && !crudRepository()")
    public Object monitorNonSpringDataRepositories(ProceedingJoinPoint joinPoint) throws Throwable {
        Tags resultTags = null;

        Instant startTime = Instant.now();
        try {
            Object result = joinPoint.proceed();
            resultTags = getResultTags(null);
            return result;

        } catch (Exception e) {
            resultTags = getResultTags(e);
            throw e;

        } finally {
            Duration executionDuration = Duration.between(startTime, Instant.now());
            Tags tags = Tags
                    .of("repository", joinPoint.getTarget().getClass().getSimpleName())
                    .and("method", joinPoint.getSignature().getName())
                    .and("state", State.SUCCESS.name())
                    .and(resultTags);

            Metrics.timer(metricName, tags).record(executionDuration);
        }
    }

    private static Tags getResultTags(Exception e) {
        if (e == null) {
            return Tags.of("state", State.SUCCESS.name())
                    .and("exception", "None");

        } else {
            return Tags.of("state", State.ERROR.name())
                    .and("exception", e.getClass().getSimpleName());
        }
    }
}
