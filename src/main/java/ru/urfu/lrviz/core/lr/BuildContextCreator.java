package ru.urfu.lrviz.core.lr;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.grammar.FirstCalculator;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarService;
import ru.urfu.lrviz.core.lr.state.name.generation.ByTransitionSymbolStateNameGenerator;
import ru.urfu.lrviz.core.lr.state.name.generation.EndToEndNumerationStateNameGenerator;
import ru.urfu.lrviz.core.lr.state.name.generation.StateNameGenerator;

import java.util.HashMap;
import java.util.Objects;

import static ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm.CLASSIC;

/**
 * Билдер контекста для построения LR-автоматов
 *
 * @author dsyromyatnikov
 * @since 22.02.2026
 */
@Service
public class BuildContextCreator {
    private final GrammarService grammarService;

    public BuildContextCreator(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    public BuildContext createContext(AutomatonType automatonType, Grammar grammar, BuildOptions options) {
        return switch (automatonType) {
            case LR_0 -> createLR0Context(options);
            case LR_1 -> createLR1Context(grammar, options);
            case LALR -> createLALR1Context(grammar, options);
        };
    }

    private BuildContext createLALR1Context(Grammar grammar, BuildOptions options) {
        BuildContext buildContext = createLR1Context(grammar, options);
        buildContext.setLalr1BuildAlgorithm(Objects.requireNonNullElse(options.lalr1BuildAlgorithm(), CLASSIC));
        return buildContext;
    }

    /**
     * @return контекст для построения LR(1)-автомата
     */
    private BuildContext createLR1Context(Grammar grammar, BuildOptions rawContext) {
        BuildContext context = createLR0Context(rawContext);
        context.setFirstCalculator(new FirstCalculator(grammarService.getFirst(grammar)));
        return context;
    }

    /**
     * @return контекст для построения LR(0)-автомата
     */
    private BuildContext createLR0Context(BuildOptions buildOptions) {
        BuildContext buildContext = new BuildContext();
        buildContext.setBuildLog(new BuildLog());
        buildContext.setStateNamesGenerator(getNameGenerator(buildOptions.namesGenerationStrategy()));
        buildContext.setNamedStates(new HashMap<>());
        buildContext.setDefinedTransitions(new HashMap<>());
        return buildContext;
    }

    private StateNameGenerator getNameGenerator(@Nullable StateNamesGenerationStrategy stateNamesGenerationStrategy) {
        return switch (stateNamesGenerationStrategy) {
            case END_TO_END_NUMERIC -> new EndToEndNumerationStateNameGenerator();
            case BY_TRANSITION_SYMBOL -> new ByTransitionSymbolStateNameGenerator();
            case null -> new ByTransitionSymbolStateNameGenerator();
        };
    }
}
