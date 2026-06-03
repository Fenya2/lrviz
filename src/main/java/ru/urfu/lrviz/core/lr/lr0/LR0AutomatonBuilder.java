package ru.urfu.lrviz.core.lr.lr0;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarService;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.AbstractLRAutomatonBuilder;
import ru.urfu.lrviz.core.lr.AutomatonType;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.LRItem;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Строит LR(0)-автомат
 *
 * @author fenya
 * @since 01.02.2026
 */
@Component
public class LR0AutomatonBuilder extends AbstractLRAutomatonBuilder {

    public LR0AutomatonBuilder(GrammarService grammarService) {
        super(grammarService);
    }

    @Override
    public AutomatonType getBuildType() {
        return AutomatonType.LR_0;
    }

    @Override
    protected LRItem getInitialItem(Rule startRule) {
        return LR0Item.ofInitial(startRule);
    }

    @Override
    protected Set<LRItem> getNewItems(Grammar grammar, LRItem processingItem, BuildContext context) {
        return processingItem.getDotSymbol() instanceof NonTerminal nonTerminal
                ? grammar.getAlternativesFor(nonTerminal).stream().map(LR0Item::ofInitial).collect(Collectors.toSet())
                : Collections.emptySet();
    }
}
