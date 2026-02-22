package ru.urfu.lrviz.core.lr.lr0;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.grammar.*;
import ru.urfu.lrviz.core.lr.*;
import ru.urfu.lrviz.core.lr.operations.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Строит LR(0)-автомат
 *
 * @author fenya
 * @since 01.02.2026
 */
@Service
public class LR0AutomatonBuilder implements LrAutomatonBuilder {

    private final GrammarService grammarService;

    public LR0AutomatonBuilder(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    @Override
    public AutomatonType getBuildType() {
        return AutomatonType.LR_0;
    }

    public LRAutomaton build(Grammar grammar, BuildContext context) {
        Grammar extendedGrammar = new Grammar(grammar);
        grammarService.extendGrammar(extendedGrammar);
        context.getBuildLog().append(new ExtendGrammarOperation());
        String startStateName = initStartState(extendedGrammar, context);
        Queue<String> processingStates = new ArrayDeque<>(Collections.singleton(startStateName));
        while (!processingStates.isEmpty()) {
            String stateName = processingStates.poll();
            context.getBuildLog().append(new StartAddNewTransitionsOperation(stateName));
            processingStates.addAll(processState(stateName, extendedGrammar, context));
        }
        return new LRAutomaton(Map.copyOf(context.getNamedStates()), Map.copyOf(context.definedTransitions()));
    }

    private String initStartState(Grammar grammar, BuildContext context) {
        checkGrammarIsExtended(grammar);

        Set<Rule> initRules = grammar.getAlternativesFor(grammar.getStartSymbol());
        Rule startRule = initRules.iterator().next();
        LRItem initialItem = getInitialItem(startRule);
        LRState startState = new LRState(Collections.singleton(initialItem));
        String stateName = context.getStateNamesGenerator().getInitAutomatonStateName();
        context.getBuildLog().append(new AddStateOperation(stateName));
        context.getBuildLog().append(new AddItemInStateOperation(stateName, initialItem));
        LRState closedState = closureState(startState, grammar, context);
        logNewItemsAddition(stateName, LRState.diff(startState, closedState), context.getBuildLog());
        context.getNamedStates().put(stateName, closedState);
        return stateName;
    }

    protected LRItem getInitialItem(Rule startRule) {
        return new LR0Item(startRule, 0);
    }

    protected void checkGrammarIsExtended(Grammar grammar) {
        if (grammarService.isGrammarExtended(grammar)) {
            return;
        }
        throw new IllegalStateException("Processing grammar is not extended");
    }

    /**
     * @return имена новых добавленных в автомат состояний при обработке состояния с именем {@code stateName}
     */
    private Set<String> processState(String stateName, Grammar grammar, BuildContext context) {
        HashSet<String> newStates = new HashSet<>();
        LRState state = context.getNamedStates().get(stateName);
        Set<LRItem> stateItems = state.items();
        Map<GrammarSymbol, Set<LRItem>> groupedByDotSymbol = groupItemsByDotSymbol(stateItems);
        for (Map.Entry<GrammarSymbol, Set<LRItem>> entry : groupedByDotSymbol.entrySet()) {
            GrammarSymbol transitionSymbol = entry.getKey();
            LRState toState = buildTargetState(grammar, entry.getValue(), context);
            if (context.getNamedStates().containsValue(toState)) {
                String toStateName = getToStateName(toState, context.getNamedStates());
                context.definedTransitions().put(new LRAutomaton.TransitionKey(stateName, transitionSymbol), toStateName);
                context.getBuildLog().append(new AddTransitionOperation(stateName, toStateName, transitionSymbol));
                continue;
            }
            String newStateName = context.getStateNamesGenerator().generate(state, toState, transitionSymbol);
            context.getNamedStates().put(newStateName, toState);
            context.getBuildLog().append(new AddStateOperation(newStateName));
            logNewItemsAddition(newStateName, toState.items(), context.getBuildLog());
            context.definedTransitions().put(new LRAutomaton.TransitionKey(stateName, transitionSymbol), newStateName);
            context.getBuildLog().append(new AddTransitionOperation(stateName, newStateName, transitionSymbol));
            newStates.add(newStateName);
        }
        return newStates;
    }

    private LRState buildTargetState(Grammar grammar, Set<LRItem> initialItems, BuildContext context) {
        return closureState(new LRState(shiftItems(initialItems)), grammar, context);
    }

    private String getToStateName(LRState toState, Map<String, LRState> namedStates) {
        return namedStates.entrySet().stream()
                .filter(entry -> toState.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow();
    }

    private static Map<GrammarSymbol, Set<LRItem>> groupItemsByDotSymbol(Set<LRItem> stateItems) {
        Map<GrammarSymbol, Set<LRItem>> grouped = new HashMap<>();
        for (LRItem item : stateItems) {
            if (item.isFinal()) {
                continue;
            }
            Rule rule = item.getRule();
            GrammarSymbol dotSymbol = rule.right().get(item.getDotIndex());
            if (!grouped.containsKey(dotSymbol)) {
                grouped.put(dotSymbol, new HashSet<>());
            }
            grouped.get(dotSymbol).add(item);
        }
        return grouped;
    }

    private Set<LRItem> shiftItems(Set<LRItem> items) {
        Set<LRItem> shiftedItems = HashSet.newHashSet(items.size());
        for (LRItem item : items) {
            shiftedItems.add(item.shift());
        }
        return shiftedItems;
    }

    private LRState closureState(LRState state, Grammar grammar, BuildContext context) {
        HashSet<LRItem> processedItems = new HashSet<>();
        Set<LRItem> newStateItems = new HashSet<>(state.items());
        Queue<LRItem> processingItems = new ArrayDeque<>(state.items());
        while (!processingItems.isEmpty()) {
            LRItem item = processingItems.poll();
            if (processedItems.contains(item) || item.isFinal()) {
                continue;
            }
            Set<LRItem> newItems = getNewItems(grammar, item, context);
            newItems.removeAll(processedItems);
            newStateItems.addAll(newItems);
            processingItems.addAll(newItems);
            processedItems.add(item);
        }
        return new LRState(newStateItems);
    }

    protected Set<LRItem> getNewItems(Grammar grammar, LRItem processingItem, BuildContext context) {
        return processingItem.getDotSymbol() instanceof NonTerminal nonTerminal
                ? grammar.getAlternativesFor(nonTerminal).stream().map(LR0Item::ofInitial).collect(Collectors.toSet())
                : Collections.emptySet();
    }

    protected static void logNewItemsAddition(String stateName, Set<LRItem> newItems, BuildLog log) {
        for (LRItem item : newItems) {
            log.append(new AddItemInStateOperation(stateName, item));
        }
    }
}
