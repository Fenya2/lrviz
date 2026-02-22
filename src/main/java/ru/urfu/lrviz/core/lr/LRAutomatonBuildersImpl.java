package ru.urfu.lrviz.core.lr;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.grammar.Grammar;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fenya
 * @since 23.02.2026
 */
@Service
public class LRAutomatonBuildersImpl implements LRAutomatonBuilders {

    private final Map<AutomatonType, LrAutomatonBuilder> builders;

    public LRAutomatonBuildersImpl(List<LrAutomatonBuilder> possibleBuilders) {
        this.builders = possibleBuilders.stream().collect(Collectors.toMap(
                LrAutomatonBuilder::getBuildType,
                Function.identity(),
                (_, _) -> {
                    throw new IllegalStateException("Only one implementation per type should be supported.");
                },
                () -> new EnumMap<>(AutomatonType.class)
        ));
    }

    @Override
    public LRAutomaton build(Grammar grammar, AutomatonType type, BuildContext context) {
        LrAutomatonBuilder builder = builders.get(type);
        if (builder == null) {
            throw new UnsupportedOperationException("Automat building with type %s not supported.".formatted(type));
        }
        return builder.build(grammar, context);
    }
}
