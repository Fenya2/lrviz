package ru.urfu.lrviz.core.lr;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.grammar.FirstCalculator;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarService;
import ru.urfu.lrviz.core.lr.state.name.generation.ByTransitionSymbolStateNameGenerator;

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
    public BuildContext createLR1Context(Grammar grammar) {
        BuildContext context = createLR0Context();
        context.setFirstCalculator(new FirstCalculator(grammarService.getFirst(grammar)));
        return context;
    }

    /**
     * @return контекст для построения LR(0)-автомата
     */
    public BuildContext createLR0Context() {
        BuildContext buildContext = new BuildContext();
        buildContext.setBuildLog(new BuildLog());
        buildContext.setStateNamesGenerator(new ByTransitionSymbolStateNameGenerator());
        buildContext.setNamedStates(new HashMap<>());
        buildContext.setDefinedTransitions(new HashMap<>());
        return buildContext;
    }
}
