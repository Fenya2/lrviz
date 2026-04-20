package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.VisualizeOptionsDto;
import ru.urfu.lrviz.render.VisualizeOptions;

/**
 * @author fenya
 * @since 05.04.2026
 */
@Component
public class VisualizeOptionsDtoConverter implements Converter<VisualizeOptionsDto, VisualizeOptions> {
    @Override
    public VisualizeOptions convert(VisualizeOptionsDto optionsDto) {
        if (optionsDto == null) {
            return null;
        }
        return VisualizeOptions.builder()
                .visualizeSpecifiedBuildLogOperations(optionsDto.visualizeOperations())
                .colorizeTransitions(optionsDto.colorizeTransitions())
                .build();
    }
}
