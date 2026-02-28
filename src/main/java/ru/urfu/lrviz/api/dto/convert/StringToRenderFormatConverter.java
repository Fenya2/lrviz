package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.graphviz.RenderFormat;

import static ru.urfu.lrviz.graphviz.RenderFormat.PNG;

/**
 * @author fenya
 * @since 28.02.2026
 */
@Component
public class StringToRenderFormatConverter implements Converter<String, RenderFormat> {
    @Override
    public RenderFormat convert(String format) {
        if (MediaType.IMAGE_PNG_VALUE.equals(format)) {
            return PNG;
        }
        throw new UnsupportedOperationException("Format '%s' not supported".formatted(format));
    }
}
