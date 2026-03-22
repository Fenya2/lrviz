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
import static ru.urfu.lrviz.core.lr.lr1.LR1AutomatonBuilder.getFirsSet;

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
        Map<String, LRState> lalr1AutomatonNamedStates = initLalr1AutomatonStates(lr0Automaton);
        Map<String, Set<LRItem>> lalr1NamedKernels = initLalr1Kernels(lalr1AutomatonNamedStates, context.getStartItem());

        for (GrammarSymbol symbol : grammar.getGrammarSymbols()) {
            for (Map.Entry<String, Set<LRItem>> namedLalr1Kernel : lalr1NamedKernels.entrySet()) {
                for (LRItem kernelItem : namedLalr1Kernel.getValue()) {
                    Set<LR1Item> closedKernel = closureKernelItem(kernelItem, grammar, context);
                    for (LR1Item item : closedKernel) {
                        if (!symbol.equals(item.getDotSymbol())) {
                            continue;
                        }
                        LookAheadSymbol lookAheadSymbol = item.getLookAheadSymbol();
                        if (!(lookAheadSymbol instanceof FictiveGrammarTerminalSymbol)) {
                            String stateName = lr0Automaton.transitions().get(new LRAutomaton.TransitionKey(namedLalr1Kernel.getKey(), symbol));
                            Set<LRItem> kernel = lalr1NamedKernels.get(stateName);
                            LRItem forGenerationItem = item.shift();
                            for (LRItem propagateCandidate : kernel) {
                                if (equalsByBasePart(forGenerationItem, propagateCandidate)) {
                                    ((LALR1Item) propagateCandidate).addLookAhead(lookAheadSymbol);
                                    break;
                                }
                            }
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
                    for (LRItem kernelItem : namedLalr1Kernel.getValue()) {
                        Set<LR1Item> closedKernel = closureKernelItem(kernelItem, grammar, context);
                        for (LR1Item item : closedKernel) {
                            if (!symbol.equals(item.getDotSymbol())) {
                                continue;
                            }
                            LookAheadSymbol lookAheadSymbol = item.getLookAheadSymbol();
                            if (lookAheadSymbol instanceof FictiveGrammarTerminalSymbol) {
                                Set<LookAheadSymbol> propagateSymbols = ((LALR1Item) kernelItem).getLookAheadSymbols();
                                String propagatedStateName = lr0Automaton.transitions().get(new LRAutomaton.TransitionKey(namedLalr1Kernel.getKey(), symbol));
                                Set<LRItem> propagatedKernel = lalr1NamedKernels.get(propagatedStateName);

                                LRItem propagateItem = item.shift();
                                for (LRItem propagateCandidate : propagatedKernel) {
                                    if (equalsByBasePart(propagateCandidate, propagateItem)) {
                                        boolean isNewPropagation = ((LALR1Item) propagateCandidate).addLookAheads(propagateSymbols);
                                        if (isNewPropagation) {
                                            stabilized = false;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } while (!stabilized);

        return new LRAutomaton(lalr1AutomatonNamedStates, lr0Automaton.transitions());
    }

    /**
     * Создает новый автомат с пустыми lalr(1)-пунктами на основе переданного lr(0)-автомата
     */
    private Map<String, LRState> initLalr1AutomatonStates(LRAutomaton lr0Automaton) {
        Map<String, LRState> namedStates = lr0Automaton.namedStates();
        Map<String, LRState> lalr1NamedStates = HashMap.newHashMap(namedStates.size());
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
    private static Set<LR1Item> closureKernelItem(LRItem kernelItem, Grammar grammar, BuildContext context) {
        Queue<LR1Item> processingItems = new ArrayDeque<>();
        processingItems.add(new LR1Item(kernelItem.getRule(), kernelItem.getDotIndex(), FictiveGrammarTerminalSymbol.getInstance()));
        Set<LR1Item> closedKernel = new LinkedHashSet<>();
        while (!processingItems.isEmpty()) {
            LR1Item item = processingItems.poll();
            if (closedKernel.contains(item) || item.isFinal()) {
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

    private static Set<LRItem> getLalr1Kernel(LRState state, LRItem startItem) {
        return state.items().stream()
                .filter(item -> equalsByBasePart(startItem, item) || !item.isDotSymbolAtTheBeginning())
                .collect(Collectors.toSet());
    }

    private static boolean equalsByBasePart(LRItem first, LRItem second) {
        return first.getRule().equals(second.getRule()) && first.getDotIndex() == second.getDotIndex();
    }
}
