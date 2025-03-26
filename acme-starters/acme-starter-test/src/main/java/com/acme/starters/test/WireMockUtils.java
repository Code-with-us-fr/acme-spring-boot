package com.acme.starters.test;

import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.common.filemaker.FilenameMaker;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import com.github.tomakehurst.wiremock.standalone.MappingsSource;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * Utility class to simplify the usage of {@link WireMockExtension}.
 */
public class WireMockUtils {

    /**
     * Create a simple {@link WireMockExtension}
     *
     * @param tenant   Tenant to use. Put mapping files into {@code src/test/resources/mappings/<tenant>} folder.
     * @param httpPort Http port for this tenant
     */
    public static WireMockExtension simpleMockExtension(String tenant, int httpPort) {
        return WireMockExtension.newInstance()
                .options(options()
                        .port(httpPort)
                        .mappingSource(tenantMappingSource(tenant))
                        .globalTemplating(true))
                .build();
    }

    /**
     * Create a {@link MappingsSource} for a tenant. Put mapping files into {@code src/test/resources/mappings/<tenant>} folder.
     */
    public static MappingsSource tenantMappingSource(String tenant) {
        return new JsonFileMappingsSource(
                new ClasspathFileSource("src/test/resources/mappings/" + tenant),
                new FilenameMaker());
    }
}
