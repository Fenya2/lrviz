package ru.urfu.lrviz.api;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.regex.Pattern;

import static ru.urfu.lrviz.api.VersionsConstants.V1;

/**
 * @author fenya
 * @since 13.02.2026
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^v\\d+$");

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .useVersionResolver(WebConfiguration::resolveVersion)
                .addSupportedVersions(V1)
                .setVersionRequired(false);
    }

    private static @Nullable String resolveVersion(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (!path.startsWith("/api")) {
            return null;
        }
        String[] parts = path.split("/");
        if (parts.length < 3) {
            return null;
        }
        String version = parts[2];
        return VERSION_PATTERN.matcher(version).matches()
                ? version
                : null;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api/{version}", HandlerTypePredicate.forAnnotation(RestController.class)
                .and(HandlerTypePredicate.forBasePackage("ru.urfu.lrviz.api")));
    }
}
