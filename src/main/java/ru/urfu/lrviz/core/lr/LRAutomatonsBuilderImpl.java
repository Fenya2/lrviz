package ru.urfu.lrviz.core.lr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LRAutomatonsBuilderImpl implements LRAutomatonsBuilder {
    private static final String INIT_AUTOMATON_STATE_NAME = "∇";
    private static final String TEMPORAL_AUTOMATON_STATE_NAME = "tmp";

    private final GrammarService grammarService;
    private final StatesNamesServiceGenerator statesNamesGenerator;

    @Autowired
    public LRAutomatonsBuilderImpl(
            GrammarService grammarService,
            StatesNamesServiceGenerator statesNamesGenerator) {
        this.grammarService = grammarService;
        this.statesNamesGenerator = statesNamesGenerator;
    }

    @Override
    public LR0AutomataBuildResult buildLR0Automata(Grammar grammar) {
        Grammar extendedGrammar = new Grammar(grammar);
        grammarService.extendGrammar(extendedGrammar);
        DFA<LR0AutomatonState, GrammarSymbol> automaton = initLR0Automaton(extendedGrammar);
        Queue<LR0AutomatonState> processingStates = initStatesProcessingQueue(automaton);
        statesNamesGenerator.execute(() -> {
            while (!processingStates.isEmpty()) {
                LR0AutomatonState currentState = processingStates.poll();
                closureState(currentState, extendedGrammar);
                processingStates.addAll(tryAddNewTransitions(currentState, extendedGrammar, automaton));
            }
        });
        return new LR0AutomataBuildResult(null, automaton);
    }

    private DFA<LR0AutomatonState, GrammarSymbol> initLR0Automaton(Grammar grammar) {
        LR0AutomatonState startState = initStartState(grammar);
        Set<GrammarSymbol> dfaAlphabet = initAlphabet(grammar);
        return new DFA<>(Collections.singleton(startState), dfaAlphabet, Collections.emptyMap(), startState,
                Collections.emptySet());
    }

    private LR0AutomatonState initStartState(Grammar grammar) {
        Set<Rule> initRules = grammar.getAlternativesFor(grammar.getStartSymbol());
        checkGrammarIsExtended(initRules);
        Rule startRule = initRules.iterator().next();
        LR0Item initialItem = new LR0Item(startRule, 0);
        return new LR0AutomatonState(INIT_AUTOMATON_STATE_NAME, Collections.singleton(initialItem));
    }

    private Set<GrammarSymbol> initAlphabet(Grammar grammar) {
        Set<GrammarSymbol> alphabet = new HashSet<>();
        alphabet.addAll(grammar.getNonTerminals());
        alphabet.addAll(grammar.getTerminals());
        return alphabet;
    }

    private void checkGrammarIsExtended(Set<Rule> initRules) {
        if (initRules.size() == 1 && initRules.iterator().next().right().size() == 1) {
            return;
        }
        throw new IllegalStateException("Processing grammar is not extended");
    }

    private static Queue<LR0AutomatonState> initStatesProcessingQueue(DFA<LR0AutomatonState, GrammarSymbol> lr0) {
        Queue<LR0AutomatonState> processingStates = new ArrayDeque<>();
        processingStates.add(lr0.getStartState());
        return processingStates;
    }

    /**
     * @return новые добавленные в автомат состояния
     */
    private Set<LR0AutomatonState> tryAddNewTransitions(LR0AutomatonState fromState, Grammar grammar, DFA<LR0AutomatonState, GrammarSymbol> automaton) {
        HashSet<LR0AutomatonState> newStates = new HashSet<>();

        Set<LR0Item> stateItems = fromState.items();
        Map<GrammarSymbol, Set<LR0Item>> groupedByDotSymbol = groupItemsByDotSymbol(stateItems);
        for (Map.Entry<GrammarSymbol, Set<LR0Item>> entry : groupedByDotSymbol.entrySet()) {
            GrammarSymbol transitionSymbol = entry.getKey();
            LR0AutomatonState newTargetStateCandidate = new LR0AutomatonState(TEMPORAL_AUTOMATON_STATE_NAME, createShiftedItems(entry.getValue()));
            closureState(newTargetStateCandidate, grammar);
            Optional<LR0AutomatonState> possibleState = automaton.getStates().stream()
                    .filter(s -> newTargetStateCandidate.items().equals(s.items())).findFirst();
            if (possibleState.isPresent()) {
                LR0AutomatonState alreadyDefinedState = possibleState.get();
                automaton.addTransition(fromState, alreadyDefinedState, transitionSymbol);
            } else {
                int newStateNumber = statesNamesGenerator.nextNumber(transitionSymbol.lexicalValue);
                LR0AutomatonState newState = new LR0AutomatonState(transitionSymbol.lexicalValue + newStateNumber, newTargetStateCandidate.items());
                automaton.addState(newState);
                automaton.addTransition(fromState, newState, transitionSymbol);
                newStates.add(newState);
            }
        }
        return newStates;
    }

    private static Map<GrammarSymbol, Set<LR0Item>> groupItemsByDotSymbol(Set<LR0Item> stateItems) {
        Map<GrammarSymbol, Set<LR0Item>> grouped = new HashMap<>();
        for (LR0Item item : stateItems) {
            if (item.isFinal()) {
                continue;
            }
            Rule rule = item.rule();
            GrammarSymbol dotSymbol = rule.right().get(item.dotIndex());
            if (!grouped.containsKey(dotSymbol)) {
                grouped.put(dotSymbol, new HashSet<>());
            }
            grouped.get(dotSymbol).add(item);
        }
        return grouped;
    }

    private Set<LR0Item> createShiftedItems(Set<LR0Item> items) {
        Set<LR0Item> shiftedItems = new HashSet<>(items.size());
        for (LR0Item item : items) {
            shiftedItems.add(new LR0Item(item.rule(), item.dotIndex() + 1));
        }
        return shiftedItems;
    }


    private void closureState(LR0AutomatonState state, Grammar grammar) {
        HashSet<LR0Item> processedItems = new HashSet<>();
        Queue<LR0Item> processingItems = new ArrayDeque<>(state.items());
        while (!processingItems.isEmpty()) {
            LR0Item item = processingItems.poll();
            if (processedItems.contains(item) || item.isFinal()) {
                continue;
            }
            Rule rule = item.rule();
            GrammarSymbol nextSymbol = rule.right().get(item.dotIndex());
            if (nextSymbol instanceof NonTerminal nonTerminal) {
                Set<LR0Item> newItems = grammar.getAlternativesFor(nonTerminal).stream()
                        .map(LR0Item::ofInitial).collect(Collectors.toSet());
                newItems.removeAll(processedItems);
                state.addItems(newItems);
                processingItems.addAll(newItems);
                processedItems.add(item);
            }
        }
    }
}
