package com.acme.starters.test.docs;

import com.acme.starters.test.docs.database.DatabaseDocumentationProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.nio.file.Files;
import java.nio.file.Path;

@ConfigurationProperties("documentation")
public class DocumentationProperties implements InitializingBean {

    /**
     * Base path of generated documentation
     */
    private Path basePath = Path.of("target", "generated-docs");

    @NestedConfigurationProperty
    private DatabaseDocumentationProperties database = new DatabaseDocumentationProperties();

    public Path getBasePath() {
        return basePath;
    }

    public DocumentationProperties setBasePath(Path basePath) {
        this.basePath = basePath;
        return this;
    }

    public DatabaseDocumentationProperties getDatabase() {
        return database;
    }

    public DocumentationProperties setDatabase(DatabaseDocumentationProperties database) {
        this.database = database;
        return this;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Files.createDirectories(basePath);
    }
}
