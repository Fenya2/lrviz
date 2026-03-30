package ru.urfu.lrviz.core.lr.lalr1;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.grammar.*;
import ru.urfu.lrviz.core.lr.*;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;
import ru.urfu.lrviz.core.lr.lr1.EndOfChainSymbol;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;
import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

import java.util.*;
import java.util.stream.Collectors;

import static ru.urfu.lrviz.core.lr.lr1.LR1AutomatonBuilder.buildChain;
import static ru.urfu.lrviz.core.lr.lr1.LR1AutomatonBuilder.getFirstSet;

/**
 * @author fenya
 * @since 21.03.2026
 */
@Component
public class ChannelLALR1AutomatonBuilder {
    /**
     * Ключ к кэшу, хранящем замыкания пункта из LALR(1)-ядра
     *
     * @param kernelName имя ядра
     * @param item       пункт ядра
     * @author fenya
     * @since 30.03.2026
     */
    public record ClosureCacheKey(String kernelName, LR1Item item) {
    }

    /**
     * @param lr0Automaton построенный lr(0)-автомат
     * @param context      контекст построения
     * @return LALR(1)-автомат
     */
    public LRAutomaton build(LRAutomaton lr0Automaton, Grammar grammar, BuildContext context) {
        Map<String, LRState> lalr1AutomatonNamedStates = initLalr1AutomatonStates(lr0Automaton);
        Map<String, Set<LRItem>> lalr1NamedKernels = initLalr1Kernels(lalr1AutomatonNamedStates, context.getStartItem());
        Map<ClosureCacheKey, Set<LR1Item>> kernelItemClosures = new HashMap<>();

        for (GrammarSymbol symbol : grammar.getGrammarSymbols()) {
            for (Map.Entry<String, Set<LRItem>> namedLalr1Kernel : lalr1NamedKernels.entrySet()) {
                String kernelName = namedLalr1Kernel.getKey();
                for (LRItem kernelItem : namedLalr1Kernel.getValue()) {
                    LR1Item closingItem = new LR1Item(kernelItem.getRule(), kernelItem.getDotIndex(), FictiveGrammarTerminalSymbol.getInstance());
                    ClosureCacheKey cacheKey = new ClosureCacheKey(kernelName, closingItem);
                    Set<LR1Item> closedItem = kernelItemClosures.computeIfAbsent(cacheKey, _ -> closureItem(closingItem, grammar, context));
                    for (LR1Item item : closedItem) {
                        if (!symbol.equals(item.getDotSymbol())) {
                            continue;
                        }
                        LookAheadSymbol lookAheadSymbol = item.getLookAheadSymbol();
                        if (!(lookAheadSymbol instanceof FictiveGrammarTerminalSymbol)) {
                            String stateForPropagation = lr0Automaton.transitions().get(new LRAutomaton.TransitionKey(kernelName, symbol));
                            Set<LRItem> stateForGeneration = lalr1NamedKernels.get(stateForPropagation);
                            LALR1Item candidateForGeneration = (LALR1Item) findCandidate(item.shift(), stateForGeneration);
                            candidateForGeneration.addLookAhead(lookAheadSymbol);
                        }
                    }
                }
            }
        }

        boolean stabilized;
        do {
            stabilized = true;
            for (GrammarSymbol symbol : grammar.getGrammarSymbols()) {
                for (Map.Entry<String, Set<LRItem>> namedLalr1Kernel : lalr1NamedKernels.entrySet()) {
                    String kernelName = namedLalr1Kernel.getKey();
                    for (LRItem kernelItem : namedLalr1Kernel.getValue()) {
                        LR1Item closingItem = new LR1Item(kernelItem.getRule(), kernelItem.getDotIndex(), FictiveGrammarTerminalSymbol.getInstance());
                        ClosureCacheKey cacheKey = new ClosureCacheKey(kernelName, closingItem);
                        Set<LR1Item> closedKernel = kernelItemClosures.computeIfAbsent(cacheKey, _ -> closureItem(closingItem, grammar, context));
                        for (LR1Item item : closedKernel) {
                            if (!symbol.equals(item.getDotSymbol())) {
                                continue;
                            }
                            LookAheadSymbol lookAheadSymbol = item.getLookAheadSymbol();
                            if (lookAheadSymbol instanceof FictiveGrammarTerminalSymbol) {
                                Set<LookAheadSymbol> propagateSymbols = ((LALR1Item) kernelItem).getLookAheadSymbols();
                                String propagatedStateName = lr0Automaton.transitions().get(new LRAutomaton.TransitionKey(namedLalr1Kernel.getKey(), symbol));
                                Set<LRItem> stateForPropagation = lalr1NamedKernels.get(propagatedStateName);
                                LALR1Item candidateForPropagation = (LALR1Item) findCandidate(item.shift(), stateForPropagation);
                                if (candidateForPropagation.addLookAheads(propagateSymbols)) {
                                    stabilized = false;
                                }
                            }
                        }
                    }
                }
            }
        } while (!stabilized);

        return new LRAutomaton(lalr1AutomatonNamedStates, lr0Automaton.transitions());
    }

    private LRItem findCandidate(LRItem itemToFind, Set<LRItem> kernelForPropagation) {
        for (LRItem candidate : kernelForPropagation) {
            if (equalsByBasePart(itemToFind, candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Can't find candidate item.");
    }

    /**
     * Создает новый автомат с пустыми lalr(1)-пунктами на основе переданного lr(0)-автомата
     */
    private Map<String, LRState> initLalr1AutomatonStates(LRAutomaton lr0Automaton) {
        Map<String, LRState> namedStates = lr0Automaton.namedStates();
        Map<String, LRState> lalr1NamedStates = LinkedHashMap.newLinkedHashMap(namedStates.size());
        for (Map.Entry<String, LRState> namedState : namedStates.entrySet()) {
            Set<LRItem> lalr1StateItems = mapLr0ItemsToEmptyLalr1Items(namedState.getValue().items());
            lalr1NamedStates.put(namedState.getKey(), new LRState(lalr1StateItems));
        }
        return lalr1NamedStates;
    }

    /**
     * Подготавливает lalr(1)-ядра (по сути строит lr(0)-ядра). Также к начальному пункту приписывает спонтанно
     * сгенерированный символ {@link EndOfChainSymbol}
     *
     * @param lalr1AutomatonStates lalr(1)-автомат
     * @param startItem            начальный пункт
     */
    private static Map<String, Set<LRItem>> initLalr1Kernels(Map<String, LRState> lalr1AutomatonStates, LRItem startItem) {
        Map<String, Set<LRItem>> lr0Kernels = HashMap.newHashMap(lalr1AutomatonStates.size());
        for (Map.Entry<String, LRState> namedState : lalr1AutomatonStates.entrySet()) {
            Set<LRItem> lalr1Kernel = getLalr1Kernel(namedState.getValue(), startItem);
            for (LRItem item : lalr1Kernel) {
                LALR1Item lalr1Item = (LALR1Item) item;
                if (equalsByBasePart(startItem, lalr1Item)) {
                    lalr1Item.addLookAhead(EndOfChainSymbol.getInstance());
                    break;
                }
            }
            lr0Kernels.put(namedState.getKey(), lalr1Kernel);
        }
        return lr0Kernels;
    }

    private static Set<LRItem> mapLr0ItemsToEmptyLalr1Items(Set<LRItem> lr0Items) {
        Set<LRItem> lalr1Kernel = LinkedHashSet.newLinkedHashSet(lr0Items.size());
        for (LRItem lr0Item : lr0Items) {
            lalr1Kernel.add(LALR1Item.fromLr0Item((LR0Item) lr0Item));
        }
        return lalr1Kernel;
    }

    /**
     * Замыкает lr-пункт по символу {@link FictiveGrammarTerminalSymbol}
     */
    private static Set<LR1Item> closureItem(LR1Item kernelItem, Grammar grammar, BuildContext context) {
        Queue<LR1Item> processingItems = new ArrayDeque<>();
        processingItems.add(kernelItem);
        Set<LR1Item> closedKernel = new LinkedHashSet<>();
        while (!processingItems.isEmpty()) {
            LR1Item item = processingItems.poll();
            if (item.isDotSymbolAtTheEnd() || closedKernel.contains(item)) {
                closedKernel.add(item);
                continue;
            }
            Set<LR1Item> newItems = getNewItems(grammar, item, context);
            processingItems.addAll(newItems);
            closedKernel.add(item);
        }
        return closedKernel;
    }

    private static Set<LR1Item> getNewItems(Grammar grammar, LRItem processingItem, BuildContext context) {
        TransitionSymbol dotSymbol = processingItem.getDotSymbol();
        if (!(dotSymbol instanceof NonTerminal)) {
            return Collections.emptySet();
        }
        List<GrammarSymbol> chain = buildChain(processingItem);
        Set<LR1Item> newItems = new HashSet<>();
        for (FirstSetMember member : getFirstSet(chain, context)) {
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

    private static Set<LRItem> getLalr1Kernel(LRState state, LRItem startItem) {
        return state.items().stream()
                .filter(item -> equalsByBasePart(startItem, item) || !item.isDotSymbolAtTheBeginning())
                .collect(Collectors.toSet());
    }

    private static boolean equalsByBasePart(LRItem first, LRItem second) {
        return first.getRule().equals(second.getRule()) && first.getDotIndex() == second.getDotIndex();
    }
}
