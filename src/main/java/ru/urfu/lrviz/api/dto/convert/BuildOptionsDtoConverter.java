package ru.urfu.lrviz.api.dto.convert;

import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.BuildOptionsDto;
import ru.urfu.lrviz.core.lr.BuildOptions;
import ru.urfu.lrviz.core.lr.StateNamesGenerationStrategy;
import ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm;

import static ru.urfu.lrviz.core.lr.StateNamesGenerationStrategy.BY_TRANSITION_SYMBOL;
import static ru.urfu.lrviz.core.lr.StateNamesGenerationStrategy.END_TO_END_NUMERIC;
import static ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm.CHANNEL;
import static ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm.CLASSIC;

/**
 * Конвертер параметров построения LR-автоматов
 *
 * @author fenya
 * @since 07.03.2026
 */
@Component
public class BuildOptionsDtoConverter implements Converter<BuildOptionsDto, BuildOptions> {

    public static final String BY_TRANSITION_SYMBOL_STRATEGY_CODE = "byTransitionSymbol";
    public static final String END_TO_END_NUMERIC_STRATEGY_CODE = "endToEndNumeric";

    public static final String LALR1_BUILD_CLASSIC_BUILD_ALGORITHM = "classic";
    public static final String LALR1_BUILD_CHANNEL_BUILD_ALGORITHM = "channel";

    @Override
    public BuildOptions convert(@Nullable BuildOptionsDto options) {
        if (options == null) {
            return null;
        }
        return BuildOptions.builder()
                .namesGenerationStrategy(getStateNamesGenerationStrategy(options.namesGenerationStrategy()))
                .lalr1BuildAlgorithm(getLalr1BuildAlgorithm(options.lalr1BuildAlgorithm()))
                .enableBuildLog(Boolean.TRUE.equals(options.enableBuildLog()))
                .build();
    }

    @Nullable
    private StateNamesGenerationStrategy getStateNamesGenerationStrategy(String strategy) {
        return switch (strategy) {
            case END_TO_END_NUMERIC_STRATEGY_CODE -> END_TO_END_NUMERIC;
            case BY_TRANSITION_SYMBOL_STRATEGY_CODE -> BY_TRANSITION_SYMBOL;
            case null -> null;
            default -> throw new IllegalArgumentException("Unsupported namesGenerationStrategy " + strategy);
        };
    }

    @Nullable
    private LALR1BuildAlgorithm getLalr1BuildAlgorithm(String algorithm) {
        return switch (algorithm) {
            case LALR1_BUILD_CLASSIC_BUILD_ALGORITHM -> CLASSIC;
            case LALR1_BUILD_CHANNEL_BUILD_ALGORITHM -> CHANNEL;
            case null -> null;
            default -> throw new IllegalArgumentException("Unsupported lalr build algorithm " + algorithm);
        };
    }
}
