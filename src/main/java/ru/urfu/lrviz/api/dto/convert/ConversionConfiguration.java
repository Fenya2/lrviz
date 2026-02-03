package ru.urfu.lrviz.api.dto.convert;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author fenya
 * @since 04.02.2026
 */
@Configuration
public class ConversionConfiguration implements WebMvcConfigurer {
    private final ApplicationContext context;
    private FormatterRegistry registry;

    public ConversionConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @EventListener(ContextRefreshedEvent.class)
    private void registerConverters() {
        context.getBeansOfType(Converter.class)
                .values()
                .forEach(registry::addConverter);
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        this.registry = registry;
    }
}
