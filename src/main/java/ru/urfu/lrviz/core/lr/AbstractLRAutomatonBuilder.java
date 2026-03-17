package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarService;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.lr.operations.*;

import java.util.*;

import static ru.urfu.lrviz.core.lr.BuildLogUtils.logNewItemsAddition;

/**
 * @author fenya
 * @since 07.03.2026
 */
public abstract class AbstractLRAutomatonBuilder implements LrAutomatonBuilder {

    private final GrammarService grammarService;

    protected AbstractLRAutomatonBuilder(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    public LRAutomaton build(Grammar grammar, BuildContext context) {
        context.getBuildLog().append(new BuildLRAutomatonOperation(getBuildType()));
        Grammar extendedGrammar = new Grammar(grammar);
        grammarService.expandGrammar(extendedGrammar);
        context.getBuildLog().append(new ExtendGrammarOperation());
        String startStateName = initStartState(extendedGrammar, context);
        Queue<String> processingStates = new ArrayDeque<>(Collections.singleton(startStateName));
        while (!processingStates.isEmpty()) {
            String stateName = processingStates.poll();
            context.getBuildLog().append(new StartAddNewTransitionsOperation(stateName));
            processingStates.addAll(processState(stateName, extendedGrammar, context));
        }
        return new LRAutomaton(Map.copyOf(context.getNamedStates()), Map.copyOf(context.getDefinedTransitions()));
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

    protected abstract LRItem getInitialItem(Rule startRule);

    private void checkGrammarIsExtended(Grammar grammar) {
        if (grammarService.isGrammarExtended(grammar)) {
            return;
        }
        throw new IllegalStateException("Processing grammar is not extended");
    }

    /**
     * @return имена новых добавленных в автомат состояний при обработке состояния с именем {@code stateName}
     */
    private Set<String> processState(String stateName, Grammar grammar, BuildContext context) {
        HashSet<String> newStates = new LinkedHashSet<>();
        LRState state = context.getNamedStates().get(stateName);
        Set<LRItem> stateItems = state.items();
        Map<GrammarSymbol, Set<LRItem>> groupedByDotSymbol = groupItemsByDotSymbol(stateItems);
        for (Map.Entry<GrammarSymbol, Set<LRItem>> entry : groupedByDotSymbol.entrySet()) {
            GrammarSymbol transitionSymbol = entry.getKey();
            LRState toState = buildTargetState(grammar, entry.getValue(), context);
            if (context.getNamedStates().containsValue(toState)) {
                String toStateName = getToStateName(toState, context.getNamedStates());
                context.getDefinedTransitions().put(new LRAutomaton.TransitionKey(stateName, transitionSymbol), toStateName);
                context.getBuildLog().append(new AddTransitionOperation(stateName, toStateName, transitionSymbol));
                continue;
            }
            String newStateName = context.getStateNamesGenerator().generate(state, toState, transitionSymbol);
            context.getNamedStates().put(newStateName, toState);
            context.getBuildLog().append(new AddStateOperation(newStateName));
            logNewItemsAddition(newStateName, toState.items(), context.getBuildLog());
            context.getDefinedTransitions().put(new LRAutomaton.TransitionKey(stateName, transitionSymbol), newStateName);
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
        Map<GrammarSymbol, Set<LRItem>> grouped = new LinkedHashMap<>();
        for (LRItem item : stateItems) {
            if (item.isFinal()) {
                continue;
            }
            Rule rule = item.getRule();
            GrammarSymbol dotSymbol = rule.right().get(item.getDotIndex());
            grouped.computeIfAbsent(dotSymbol, _ -> new LinkedHashSet<>()).add(item);
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
        HashSet<LRItem> processedItems = new LinkedHashSet<>();
        Set<LRItem> newStateItems = new LinkedHashSet<>(state.items());
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

    protected abstract Set<LRItem> getNewItems(Grammar grammar, LRItem processingItem, BuildContext context);
}
