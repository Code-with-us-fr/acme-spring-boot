package com.acme.starters.test.docs;

import com.acme.starters.test.docs.database.DatabaseDocumentationAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DocumentationProperties.class)
@Import({DatabaseDocumentationAutoConfiguration.class})
public class DocumentationAutoConfiguration {
}
