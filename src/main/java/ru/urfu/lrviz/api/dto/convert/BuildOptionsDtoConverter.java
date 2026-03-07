package ru.urfu.lrviz.api.dto.convert;

import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.BuildOptionsDto;
import ru.urfu.lrviz.core.lr.BuildOptions;

import static ru.urfu.lrviz.core.lr.StateNamesGenerationStrategy.BY_TRANSITION_SYMBOL;
import static ru.urfu.lrviz.core.lr.StateNamesGenerationStrategy.END_TO_END_NUMERIC;

/**
 *
 * @author fenya
 * @since 07.03.2026
 */
@Component
public class BuildOptionsDtoConverter implements Converter<BuildOptionsDto, BuildOptions> {

    public static final String BY_TRANSITION_SYMBOL_STRATEGY_CODE = "byTransitionSymbol";
    public static final String END_TO_END_NUMERIC_STRATEGY_CODE = "endToEndNumeric";

    @Override
    public BuildOptions convert(@Nullable BuildOptionsDto source) {
        if (source == null) {
            return BuildOptions.createEmpty();
        }
        String namesGenerationStrategy = source.namesGenerationStrategy();
        if (END_TO_END_NUMERIC_STRATEGY_CODE.equals(namesGenerationStrategy)) {
            return new BuildOptions(END_TO_END_NUMERIC);
        }
        if (BY_TRANSITION_SYMBOL_STRATEGY_CODE.equals(namesGenerationStrategy)) {
            return new BuildOptions(BY_TRANSITION_SYMBOL);
        }
        throw new IllegalArgumentException("Unsupported namesGenerationStrategy " + namesGenerationStrategy);
    }
}
