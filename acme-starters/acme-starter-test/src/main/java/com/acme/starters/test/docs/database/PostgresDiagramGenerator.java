package com.acme.starters.test.docs.database;

import com.acme.starters.test.docs.DocumentationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.PropertyResolver;
import schemacrawler.schemacrawler.GrepOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.tools.command.text.diagram.options.DiagramOutputFormat;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.options.Config;
import schemacrawler.tools.options.OutputOptionsBuilder;
import us.fatehi.utility.datasource.DatabaseConnectionSources;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Generate a diagram for the Postgres database.
 */
public class PostgresDiagramGenerator implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger log = LoggerFactory.getLogger(PostgresDiagramGenerator.class);

    private final DataSource dataSource;
    private final DocumentationProperties documentationProperties;
    private final PropertyResolver propertyResolver;

    public PostgresDiagramGenerator(DataSource dataSource, DocumentationProperties documentationProperties, PropertyResolver propertyResolver) {
        this.dataSource = dataSource;
        this.documentationProperties = documentationProperties;
        this.propertyResolver = propertyResolver;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent unused) {
        Path diagramPath = documentationProperties.getBasePath().resolve(documentationProperties.getDatabase().getOutputFile());

        if (diagramPath.toFile().exists()) {
            log.info("Database diagram already exists at {}", diagramPath);
            return;
        }

        String schema = documentationProperties.getDatabase().getSchema();
        log.info("Generate database diagram for schema {}", schema);

        try {
            getSchemaCrawlerExecutable(schema, diagramPath).execute();
        } catch (Exception e) {
            log.warn("Impossible to generate database diagram", e);
        }
    }

    private SchemaCrawlerExecutable getSchemaCrawlerExecutable(String schema, Path diagramPath) {
        SchemaCrawlerExecutable executable = new SchemaCrawlerExecutable("details");

        executable.setSchemaCrawlerOptions(SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                .withGrepOptions(GrepOptionsBuilder.builder()
                        .includeGreppedTables(Pattern.compile(schema + "\\..*"))
                        .toOptions()));

        executable.setOutputOptions(OutputOptionsBuilder
                .builder()
                .withOutputFormat(DiagramOutputFormat.png)
                .withOutputFile(diagramPath)
                .title(propertyResolver.getProperty("spring.application.name", "Some application ") + " database model")
                .toOptions());

        executable.setDataSource(DatabaseConnectionSources.fromDataSource(dataSource));
        executable.setAdditionalConfiguration(new Config(Map.of("portable-names", true)));

        return executable;
    }
}
