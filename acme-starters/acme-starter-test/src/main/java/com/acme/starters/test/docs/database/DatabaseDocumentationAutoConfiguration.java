package com.acme.starters.test.docs.database;

import com.acme.starters.test.docs.DocumentationProperties;
import org.postgresql.Driver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.PropertyResolver;

import javax.sql.DataSource;

@AutoConfiguration
@ConditionalOnProperty(name = "documentation.database.enabled")
public class DatabaseDocumentationAutoConfiguration {

    @Bean
    @ConditionalOnClass(Driver.class)
    @ConditionalOnBean(DataSource.class)
    PostgresDiagramGenerator postgresDiagramGenerator(DataSource dataSource,
                                                      DocumentationProperties documentationProperties,
                                                      PropertyResolver propertyResolver) {

        return new PostgresDiagramGenerator(dataSource, documentationProperties , propertyResolver);
    }
}
