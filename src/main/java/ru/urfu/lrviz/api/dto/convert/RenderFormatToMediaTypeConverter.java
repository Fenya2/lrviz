package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.graphviz.RenderFormat;

/**
 * @author fenya
 * @since 28.02.2026
 */
@Component
public class RenderFormatToMediaTypeConverter implements Converter<RenderFormat, MediaType> {
    @Override
    public MediaType convert(RenderFormat format) {
        return switch (format) {
            case PNG -> MediaType.IMAGE_PNG;
        };
    }
}
