package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.VisualizeOptionsDto;
import ru.urfu.lrviz.render.StateNameStyle;
import ru.urfu.lrviz.render.VisualizeOptions;

/**
 * @author fenya
 * @since 05.04.2026
 */
@Component
public class VisualizeOptionsDtoConverter implements Converter<VisualizeOptionsDto, VisualizeOptions> {
    public static final String STATE_NAME_STYLE_ON_BLACK_BACKGROUND = "onBlackBackground";
    private static final String STATE_NAME_STYLE_BOLD_ON_WHITE_BACKGROUND = "boldOnWhiteBackground";

    @Override
    public VisualizeOptions convert(VisualizeOptionsDto optionsDto) {
        if (optionsDto == null) {
            return null;
        }
        VisualizeOptions.VisualizeParametersBuilder builder = VisualizeOptions.builder()
                .visualizeSpecifiedBuildLogOperations(optionsDto.visualizeOperations());
        if (optionsDto.stateNameStyle() != null) {
            switch (optionsDto.stateNameStyle()) {
                case STATE_NAME_STYLE_ON_BLACK_BACKGROUND -> builder.stateNameStyle(StateNameStyle.ON_BLACK_BACKGROUND);
                case STATE_NAME_STYLE_BOLD_ON_WHITE_BACKGROUND ->
                        builder.stateNameStyle(StateNameStyle.BOLD_ON_WHITE_BACKGROUND);
                default ->
                        throw new IllegalArgumentException("Unsupported state name style: " + optionsDto.stateNameStyle());
            }
        }
        if (optionsDto.colorizeTransitions() != null) {
            builder.colorizeTransitions(optionsDto.colorizeTransitions());
        }
        if (optionsDto.colorizeStateNames() != null) {
            builder.colorizeStateNames(optionsDto.colorizeStateNames());
        }
        return builder.build();
    }
}
