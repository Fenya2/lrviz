package ru.urfu.lrviz.core.lr.lr1;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.grammar.*;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.TransitionSymbol;
import ru.urfu.lrviz.core.lr.lr0.LR0AutomatonBuilder;

import java.util.*;
import java.util.stream.Stream;

/**
 * Строит LR(1)-автомат
 *
 * @author fenya
 * @since 14.02.2026
 */
@Service
public class LR1AutomatonBuilder extends LR0AutomatonBuilder {
    public LR1AutomatonBuilder(GrammarService grammarService) {
        super(grammarService);
    }

    @Override
    protected LRItem getInitialItem(Rule startRule) {
        return new LR1Item(startRule, 0, EndOfChainSymbol.getInstance());
    }

    @Override
    protected Set<LRItem> getNewItems(Grammar grammar, LRItem processingItem, BuildContext context) {
        TransitionSymbol dotSymbol = processingItem.getDotSymbol();
        if (!(dotSymbol instanceof NonTerminal)) {
            return Collections.emptySet();
        }
        List<GrammarSymbol> chain = buildChain(processingItem);
        Set<LRItem> newItems = new HashSet<>();
        for (FirstSetMember member : getFirstMembers(context, chain)) {
            LookAheadSymbol lookAheadSymbol = switch (member) {
                case Terminal terminal -> terminal;
                case Epsilon _ -> EndOfChainSymbol.getInstance();
                default -> throw new IllegalArgumentException();
            };
            for (Rule rule : grammar.getAlternativesFor((NonTerminal) dotSymbol)) {
                newItems.add(LR1Item.ofInitial(rule, lookAheadSymbol));
            }
        }
        return newItems;
    }

    private static List<GrammarSymbol> buildChain(LRItem processingItem) {
        Stream<GrammarSymbol> endOfChain = ((LR1Item) processingItem).getLookAheadSymbol() instanceof Terminal terminal
                ? Stream.of(terminal)
                : Stream.empty();
        return Stream.concat(
                processingItem.getRule().right().stream().skip(processingItem.getDotIndex() + 1L), endOfChain).toList();
    }

    private static Set<FirstSetMember> getFirstMembers(BuildContext context, List<GrammarSymbol> chain) {
        return Objects.requireNonNull(context.getFirstCalculator()).calculateForChain(chain);
    }
}
