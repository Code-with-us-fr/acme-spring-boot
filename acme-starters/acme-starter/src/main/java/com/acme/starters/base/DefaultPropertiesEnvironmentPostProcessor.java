package com.acme.starters.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * {@link EnvironmentPostProcessor} that add default YAML {@link PropertySource}s to current {@link ConfigurableEnvironment}.
 * <p>
 * This {@link EnvironmentPostProcessor} manage the properties {@code spring.config.activate.on-cloud-platform} and
 * {@code spring.config.activate.on-profile}, to only add the {@link PropertySource} only if {@link CloudPlatform} and
 * {@link Profiles} conditions are fulfill.
 */
public class DefaultPropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DEFAULT_PROPERTIES_FILES_LOCATION_PATTERN = "classpath*:config/application-acme-*.yml";
    private static final String SPRING_CONFIG_ACTIVATE_ON_CLOUD_PLATFORM = "spring.config.activate.on-cloud-platform";
    private static final String SPRING_CONFIG_ACTIVATE_ON_PROFILE = "spring.config.activate.on-profile";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Resource[] defaultPropertiesResources = findDefaultPropertiesResources();

            for (Resource defaultPropertiesResource : defaultPropertiesResources) {
                List<PropertySource<?>> propertySourcesInResource = getPropertySourcesFromResource(defaultPropertiesResource);

                for (PropertySource<?> propertySource : propertySourcesInResource) {
                    PropertySourcesPropertyResolver propertyResolver = getPropertyResolverFor(propertySource);
                    if (isCloudPlatformActive(environment, propertyResolver) && isProfileActive(environment, propertyResolver)) {
                        environment.getPropertySources().addLast(propertySource);
                    }
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Resource[] findDefaultPropertiesResources() throws IOException {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(DEFAULT_PROPERTIES_FILES_LOCATION_PATTERN);
        // Assure to have a reproductive sort of default properties resources
        Arrays.sort(resources, comparing(Resource::getFilename));
        return resources;
    }

    private List<PropertySource<?>> getPropertySourcesFromResource(Resource resource) throws IOException {
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        return yamlPropertySourceLoader.load("Acme Boot default properties from " + resource.getFilename(), resource);
    }

    private PropertySourcesPropertyResolver getPropertyResolverFor(PropertySource<?> propertySource) {
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(propertySource);
        return new PropertySourcesPropertyResolver(propertySources);
    }

    private boolean isCloudPlatformActive(ConfigurableEnvironment environment, PropertySourcesPropertyResolver propertyResolver) {
        CloudPlatform activeCloudPlatform = CloudPlatform.getActive(environment);
        String propertiesCloudPlatform = propertyResolver.getProperty(SPRING_CONFIG_ACTIVATE_ON_CLOUD_PLATFORM, String.class);

        return propertiesCloudPlatform == null || activeCloudPlatform == CloudPlatform.valueOf(propertiesCloudPlatform.toUpperCase());
    }

    private boolean isProfileActive(ConfigurableEnvironment environment, PropertySourcesPropertyResolver propertyResolver) {
        String[] activeProfiles = environment.getActiveProfiles();
        String[] propertiesProfiles = propertyResolver.getProperty(SPRING_CONFIG_ACTIVATE_ON_PROFILE, String[].class);

        return ObjectUtils.isEmpty(propertiesProfiles) || Profiles.of(propertiesProfiles).matches(p -> ObjectUtils.containsElement(activeProfiles, p));
    }
}
