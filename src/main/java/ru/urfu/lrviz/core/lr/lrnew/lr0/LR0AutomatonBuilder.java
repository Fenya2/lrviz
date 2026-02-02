package ru.urfu.lrviz.core.lr.lrnew.lr0;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.grammar.*;
import ru.urfu.lrviz.core.lr.BuildLog;
import ru.urfu.lrviz.core.lr.lrnew.BuildContext;
import ru.urfu.lrviz.core.lr.lrnew.LRAutomaton;
import ru.urfu.lrviz.core.lr.lrnew.LRItem;
import ru.urfu.lrviz.core.lr.lrnew.LRState;
import ru.urfu.lrviz.core.lr.operations.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author fenya
 * @since 01.02.2026
 */
@Service
public class LR0AutomatonBuilder {
    private static final String INIT_AUTOMATON_STATE_NAME = "∇";

    private final GrammarService grammarService;

    public LR0AutomatonBuilder(GrammarService grammarService) {
        this.grammarService = grammarService;
    }

    public LRAutomaton build(Grammar grammar, BuildContext context) {
        Grammar extendedGrammar = new Grammar(grammar);
        context.buildLog().append(new ExtendGrammarOperation());
        grammarService.extendGrammar(extendedGrammar);
        String startState = initStartState(extendedGrammar, context);
        Queue<String> processingStates = new ArrayDeque<>(Collections.singleton(startState));
        while (!processingStates.isEmpty()) {
            String processingStateName = processingStates.poll();
            context.buildLog().append(new StartAddNewTransitions(processingStateName));
            processingStates.addAll(tryAddNewTransitions(processingStateName, extendedGrammar, context));
        }
        return new LRAutomaton(Map.copyOf(context.namedStates()), Map.copyOf(context.definedTransitions()));
    }

    private String initStartState(Grammar grammar, BuildContext context) {
        Set<Rule> initRules = grammar.getAlternativesFor(grammar.getStartSymbol());
        checkGrammarIsExtended(initRules);
        Rule startRule = initRules.iterator().next();
        LR0Item initialItem = new LR0Item(startRule, 0);
        LRState startState = new LRState(Collections.singleton(initialItem));
        context.buildLog().append(new AddStateOperation(INIT_AUTOMATON_STATE_NAME));
        context.buildLog().append(new AddItemInStateOperation(INIT_AUTOMATON_STATE_NAME, initialItem));
        LRState closedState = closureState(startState, grammar);
        logNewItemsAddition(INIT_AUTOMATON_STATE_NAME, LRState.diff(startState, closedState), context.buildLog());
        context.namedStates().put(INIT_AUTOMATON_STATE_NAME, closedState);
        return INIT_AUTOMATON_STATE_NAME;
    }

    private void checkGrammarIsExtended(Set<Rule> initRules) {
        if (initRules.size() == 1 && initRules.iterator().next().right().size() == 1) {
            return;
        }
        throw new IllegalStateException("Processing grammar is not extended");
    }

    /**
     * @return новые добавленные в автомат состояния
     */
    private Set<String> tryAddNewTransitions(String fromStateName, Grammar grammar, BuildContext context) {
        HashSet<String> newStates = new HashSet<>();
        LRState fromState = context.namedStates().get(fromStateName);
        Set<LRItem> stateItems = fromState.items();
        Map<GrammarSymbol, Set<LRItem>> groupedByDotSymbol = groupItemsByDotSymbol(stateItems);
        for (Map.Entry<GrammarSymbol, Set<LRItem>> entry : groupedByDotSymbol.entrySet()) {
            GrammarSymbol transitionSymbol = entry.getKey();
            context.buildLog().append(new CheckForNewStateOperation(transitionSymbol, fromStateName));
            LRState toState = buildToState(grammar, entry.getValue());
            if (context.namedStates().containsValue(toState)) {
                String toStateName = getToStateName(toState, context.namedStates());
                context.definedTransitions().put(new LRAutomaton.TransitionKey(fromStateName, transitionSymbol), toStateName);
                context.buildLog().append(new AddTransitionOperation(fromStateName, toStateName, transitionSymbol));
                continue;
            }
            int newStateNumber = context.stateNamesCounter().nextNumber(transitionSymbol.lexicalValue);
            String newStateName = transitionSymbol.lexicalValue + newStateNumber;
            context.namedStates().put(newStateName, toState);
            context.buildLog().append(new ConfirmNeedNewStateOperation());
            context.buildLog().append(new AddStateOperation(newStateName));
            logNewItemsAddition(newStateName, toState.items(), context.buildLog());
            context.definedTransitions().put(new LRAutomaton.TransitionKey(fromStateName, transitionSymbol), newStateName);
            context.buildLog().append(new AddTransitionOperation(fromStateName, newStateName, transitionSymbol));
            newStates.add(newStateName);
        }
        return newStates;
    }

    private @NonNull LRState buildToState(Grammar grammar, Set<LRItem> initialItems) {
        return closureState(new LRState(shiftItems(initialItems)), grammar);
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
        Set<LRItem> shiftedItems = new HashSet<>(items.size());
        for (LRItem item : items) {
            shiftedItems.add(item.shift());
        }
        return shiftedItems;
    }

    private LRState closureState(LRState state, Grammar grammar) {
        HashSet<LRItem> processedItems = new HashSet<>();
        Set<LRItem> currentStateItems = state.items();
        Queue<LRItem> processingItems = new ArrayDeque<>(currentStateItems);
        Set<LRItem> newStateItems = new HashSet<>(currentStateItems);
        while (!processingItems.isEmpty()) {
            LRItem item = processingItems.poll();
            if (processedItems.contains(item) || item.isFinal()) {
                continue;
            }
            Rule rule = item.getRule();
            GrammarSymbol nextSymbol = rule.right().get(item.getDotIndex());
            if (nextSymbol instanceof NonTerminal nonTerminal) {
                Set<LRItem> newItems = grammar.getAlternativesFor(nonTerminal).stream()
                        .map(LR0Item::ofInitial).collect(Collectors.toSet());
                newItems.removeAll(processedItems);
                newStateItems.addAll(newItems);
                processingItems.addAll(newItems);
                processedItems.add(item);
            }
        }
        return new LRState(newStateItems);
    }

    private static void logNewItemsAddition(String stateName, Set<LRItem> newItems, BuildLog log) {
        for (LRItem item : newItems) {
            log.append(new AddItemInStateOperation(stateName, item));
        }
    }
}
