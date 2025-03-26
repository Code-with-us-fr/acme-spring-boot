package com.acme.starters.test.docs.database;

import java.nio.file.Path;

public class DatabaseDocumentationProperties {

    /**
     * Activate generation of database diagram
     */
    private boolean enabled = false;

    /**
     * Diagram output file
     */
    private Path outputFile = Path.of("database.png");

    /**
     * Schema to document
     */
    private String schema = "public";

    public boolean isEnabled() {
        return enabled;
    }

    public DatabaseDocumentationProperties setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public DatabaseDocumentationProperties setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public DatabaseDocumentationProperties setSchema(String schema) {
        this.schema = schema;
        return this;
    }
}
