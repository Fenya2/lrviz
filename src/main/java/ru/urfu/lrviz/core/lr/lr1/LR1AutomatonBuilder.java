package ru.urfu.lrviz.core.lr.lr1;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.grammar.*;
import ru.urfu.lrviz.core.lr.*;

import java.util.*;
import java.util.stream.Stream;

/**
 * Строит LR(1)-автомат
 *
 * @author fenya
 * @since 14.02.2026
 */
@Component
public class LR1AutomatonBuilder extends AbstractLRAutomatonBuilder {
    public LR1AutomatonBuilder(GrammarService grammarService) {
        super(grammarService);
    }

    @Override
    public AutomatonType getBuildType() {
        return AutomatonType.LR_1;
    }

    @Override
    protected LRItem getInitialItem(Rule startRule) {
        return LR1Item.ofInitial(startRule, EndOfChainSymbol.getInstance());
    }

    @Override
    protected Set<LRItem> getNewItems(Grammar grammar, LRItem processingItem, BuildContext context) {
        TransitionSymbol dotSymbol = processingItem.getDotSymbol();
        if (!(dotSymbol instanceof NonTerminal)) {
            return Collections.emptySet();
        }
        List<GrammarSymbol> chain = buildChain(processingItem);
        Set<LRItem> newItems = new HashSet<>();
        for (FirstSetMember member : getFirsSet(chain, context)) {
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

    private static Set<FirstSetMember> getFirsSet(List<GrammarSymbol> chain, BuildContext context) {
        return Objects.requireNonNull(context.getFirstCalculator()).calculateForChain(chain);
    }
}
