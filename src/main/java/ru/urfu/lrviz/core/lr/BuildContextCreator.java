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

    /**
     * @return контекст для построения LR(1)-автомата
     */
    public BuildContext createLR1Context(Grammar grammar, BuildOptions rawContext) {
        BuildContext context = createLR0Context(rawContext);
        context.setFirstCalculator(new FirstCalculator(grammarService.getFirst(grammar)));
        return context;
    }

    /**
     * @return контекст для построения LR(0)-автомата
     */
    public BuildContext createLR0Context(BuildOptions buildOptions) {
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
