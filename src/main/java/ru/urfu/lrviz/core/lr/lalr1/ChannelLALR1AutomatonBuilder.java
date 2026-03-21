package ru.urfu.lrviz.core.lr.lalr1;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author fenya
 * @since 21.03.2026
 */
@Component
public class ChannelLALR1AutomatonBuilder {
    /**
     * @param lr0Automaton построенный lr(0)-автомат
     * @param context      контекст построения
     * @return LALR(1)-автомат
     */
    public LRAutomaton build(LRAutomaton lr0Automaton, Grammar grammar, BuildContext context) {
        Map<String, Set<LRItem>> lr0Kernels = buildLr0KernelSets(lr0Automaton, grammar);



        return null;
    }

    private static Map<String, Set<LRItem>> buildLr0KernelSets(LRAutomaton lr0Automaton, Grammar grammar) {
        return lr0Automaton.namedStates().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getLr0Kernel(entry.getValue(), grammar)));


    }

    private static Set<LRItem> getLr0Kernel(LRState state, Grammar grammar) {
        return state.items().stream()
                .filter(item -> isStartItem(item, grammar) || !item.isDotSymbolAtTheBeginning())
                .collect(Collectors.toSet());
    }

    private static boolean isStartItem(LRItem item, Grammar grammar) {
        return item.getRule().left().equals(grammar.getStartSymbol());
    }
}
