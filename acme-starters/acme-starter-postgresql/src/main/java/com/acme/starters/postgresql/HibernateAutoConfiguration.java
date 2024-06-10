package com.acme.starters.postgresql;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Map;

@AutoConfiguration
public class HibernateAutoConfiguration {

    /**
     * <p>{@link HibernatePropertiesCustomizer} that provides a default Spring {@link com.fasterxml.jackson.databind.ObjectMapper} for Hibernate JSON mapping.
     * Using the {@link ObjectMapper} configure by Spring WebMvc.</p>
     */
    @Bean
    @ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
    public HibernatePropertiesCustomizer jsonFormatMapperCustomizer(Jackson2ObjectMapperBuilder builder) {
        return hibernateProperties ->
                addJsonMapperIfAbsent(hibernateProperties, builder.build());
    }

    /**
     * <p>{@link HibernatePropertiesCustomizer} that provides a default Spring {@link com.fasterxml.jackson.databind.ObjectMapper} for Hibernate JSON mapping.
     * Using a new {@link ObjectMapper}.</p>
     */
    @Bean
    @ConditionalOnMissingClass("org.springframework.http.converter.json.Jackson2ObjectMapperBuilder")
    public HibernatePropertiesCustomizer jackson2ObjectMapperBuilder() {
        return hibernateProperties ->
                addJsonMapperIfAbsent(hibernateProperties, new ObjectMapper().findAndRegisterModules());
    }

    private static void addJsonMapperIfAbsent(Map<String, Object> hibernateProperties, ObjectMapper objectMapper) {
        hibernateProperties.computeIfAbsent(
                AvailableSettings.JSON_FORMAT_MAPPER,
                __ -> new JacksonJsonFormatMapper(objectMapper));
    }
}
