package ru.urfu.lrviz.core.lr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.*;
import ru.urfu.lrviz.core.lr.operations.*;

import java.util.*;
import java.util.stream.Collectors;

import static ru.urfu.lrviz.core.lr.AutomatonType.LR_0;

@Service
public class LR0AutomatonsBuilder implements LRAutomatonsBuilder<LR0AutomatonState> {
    private static final String INIT_AUTOMATON_STATE_NAME = "∇";
    private static final String TEMPORAL_AUTOMATON_STATE_NAME = "tmp";

    private final GrammarService grammarService;
    private final StateNamesCounter stateNamesCounter;

    @Autowired
    public LR0AutomatonsBuilder(
            GrammarService grammarService,
            StateNamesCounter stateNamesCounter) {
        this.grammarService = grammarService;
        this.stateNamesCounter = stateNamesCounter;
    }

    @Override
    public AutomatonType getBuildType() {
        return LR_0;
    }

    @Override
    public DFA<LR0AutomatonState, GrammarSymbol> build(Grammar grammar, BuildContext context) {
        BuildLog log = context.buildLog();
        Grammar extendedGrammar = new Grammar(grammar);
        log.append(new ExtendGrammarOperation());
        grammarService.extendGrammar(extendedGrammar);
        DFA<LR0AutomatonState, GrammarSymbol> DFA = initLR0Automaton(extendedGrammar, log);
        Queue<LR0AutomatonState> processingStates = initStatesProcessingQueue(DFA);
        stateNamesCounter.execute(() -> {
            while (!processingStates.isEmpty()) {
                LR0AutomatonState currentState = processingStates.poll();
                log.append(new StartAddNewTransitions(currentState));
                processingStates.addAll(tryAddNewTransitions(currentState, extendedGrammar, DFA, log));
            }
        });
        return DFA;
    }

    private DFA<LR0AutomatonState, GrammarSymbol> initLR0Automaton(Grammar grammar, BuildLog log) {
        LR0AutomatonState startState = initStartState(grammar);
        log.append(new AddStateOperation(startState));
        log.append(new AddItemInStateOperation(startState, startState.getItems().stream().findFirst().orElseThrow()));
        closureState(startState, grammar, log);
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
    private Set<LR0AutomatonState> tryAddNewTransitions(LR0AutomatonState fromState, Grammar grammar, DFA<LR0AutomatonState, GrammarSymbol> DFA, BuildLog log) {
        HashSet<LR0AutomatonState> newStates = new HashSet<>();

        Set<LR0Item> stateItems = fromState.getItems();
        Map<GrammarSymbol, Set<LR0Item>> groupedByDotSymbol = groupItemsByDotSymbol(stateItems);
        for (Map.Entry<GrammarSymbol, Set<LR0Item>> entry : groupedByDotSymbol.entrySet()) {
            GrammarSymbol transitionSymbol = entry.getKey();
            LR0AutomatonState newTargetStateCandidate = new LR0AutomatonState(TEMPORAL_AUTOMATON_STATE_NAME, createShiftedItems(entry.getValue()));
            log.append(new CheckForNewStateOperation(transitionSymbol));
            closureState(newTargetStateCandidate, grammar, new BuildLog());
            Optional<LR0AutomatonState> possibleState = DFA.getStates().stream()
                    .filter(s -> newTargetStateCandidate.getItems().equals(s.getItems())).findFirst();
            if (possibleState.isPresent()) {
                LR0AutomatonState alreadyDefinedState = possibleState.get();
                DFA.addTransition(fromState, alreadyDefinedState, transitionSymbol);
                log.append(new ConfirmStateAlreadyExist(alreadyDefinedState));
                log.append(new AddTransitionOperation(fromState, alreadyDefinedState, transitionSymbol));
                continue;
            }
            int newStateNumber = stateNamesCounter.nextNumber(transitionSymbol.lexicalValue);
            Set<LR0Item> newStateItems = newTargetStateCandidate.getItems();
            LR0AutomatonState newState = new LR0AutomatonState(transitionSymbol.lexicalValue + newStateNumber, newStateItems);
            DFA.addState(newState);
            log.append(new ConfirmNeedNewStateOperation());
            log.append(new AddStateOperation(newState));
            logNewItemsAddition(newState, newStateItems, log);
            DFA.addTransition(fromState, newState, transitionSymbol);
            log.append(new AddTransitionOperation(fromState, newState, transitionSymbol));
            newStates.add(newState);
        }
        return newStates;
    }

    private static Map<GrammarSymbol, Set<LR0Item>> groupItemsByDotSymbol(Set<LR0Item> stateItems) {
        Map<GrammarSymbol, Set<LR0Item>> grouped = new HashMap<>();
        for (LR0Item item : stateItems) {
            if (item.isFinal()) {
                continue;
            }
            Rule rule = item.getRule();
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
            shiftedItems.add(new LR0Item(item.getRule(), item.dotIndex() + 1));
        }
        return shiftedItems;
    }

    private void closureState(LR0AutomatonState state, Grammar grammar, BuildLog log) {
        log.append(new StartStateClosureOperation(state));
        HashSet<LR0Item> processedItems = new HashSet<>();
        Queue<LR0Item> processingItems = new ArrayDeque<>(state.getItems());
        while (!processingItems.isEmpty()) {
            LR0Item item = processingItems.poll();
            if (processedItems.contains(item) || item.isFinal()) {
                continue;
            }
            Rule rule = item.getRule();
            GrammarSymbol nextSymbol = rule.right().get(item.dotIndex());
            if (nextSymbol instanceof NonTerminal nonTerminal) {
                Set<LR0Item> newItems = grammar.getAlternativesFor(nonTerminal).stream()
                        .map(LR0Item::ofInitial).collect(Collectors.toSet());
                newItems.removeAll(processedItems);
                logNewItemsAddition(state, newItems, log);
                state.addItems(newItems);
                processingItems.addAll(newItems);
                processedItems.add(item);
            }
        }
    }

    private static void logNewItemsAddition(LR0AutomatonState state, Set<LR0Item> newItems, BuildLog log) {
        for (LR0Item item : newItems) {
            log.append(new AddItemInStateOperation(state, item));
        }
    }
}
