package com.studyplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Web configuration for serving static resources and handling Single Page Application routing
 * within the /api context path
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // No need to override addResourceHandlers as static resources are now handled by StaticResourceConfig
    // This class is kept for potential future API-specific configurations

    /**
     * Resource resolver for Single Page Application routing
     * Forwards all requests that don't match a resource to index.html
     */
    private static class PushStateResourceResolver implements ResourceResolver {
        private final Resource index = new ClassPathResource("/static/index.html");
        private final List<String> handledExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "woff", "woff2", "appcache", "jpg", "jpeg", "gif", "ico");
        private final List<String> ignoredPaths = Arrays.asList("/api");

        @Override
        public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
            return resolve(requestPath, locations);
        }

        @Override
        public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resolvedResource = resolve(resourcePath, locations);
            if (resolvedResource == null) {
                return null;
            }
            try {
                return resolvedResource.getURL().toString();
            } catch (IOException e) {
                return resolvedResource.getFilename();
            }
        }

        private Resource resolve(String requestPath, List<? extends Resource> locations) {
            // Skip API paths
            if (isIgnoredPath(requestPath)) {
                return null;
            }

            // Check if the path has a file extension
            if (isHandledExtension(requestPath)) {
                return locations.stream()
                        .map(loc -> createRelative(loc, requestPath))
                        .filter(resource -> resource != null && resource.exists())
                        .findFirst()
                        .orElse(null);
            }

            // Forward to index.html for SPA routing
            return index;
        }

        private Resource createRelative(Resource resource, String relativePath) {
            try {
                return resource.createRelative(relativePath);
            } catch (IOException e) {
                return null;
            }
        }

        private boolean isIgnoredPath(String path) {
            return ignoredPaths.stream().anyMatch(path::startsWith);
        }

        private boolean isHandledExtension(String path) {
            String extension = getExtension(path);
            return extension.isEmpty() || handledExtensions.contains(extension);
        }

        private String getExtension(String path) {
            String extension = "";
            int lastDotIndex = path.lastIndexOf('.');
            if (lastDotIndex > 0) {
                extension = path.substring(lastDotIndex + 1).toLowerCase();
            }
            return extension;
        }
    }
}
