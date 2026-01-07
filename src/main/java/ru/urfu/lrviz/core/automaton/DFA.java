package ru.urfu.lrviz.core.automaton;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Детерменированный конечный автомат
 */
public final class DFA<State, Symbol> {
    public static final String EPSILON_TRANSITION_SYMBOL = "ε";

    private final Set<State> states;
    private final Set<Symbol> alphabet;
    private final Map<DFATransitionMapKey<State, Symbol>, State> transitionMap;
    private final State startState;
    private final Set<State> finalStates;

    public DFA(Set<State> states, Set<Symbol> alphabet, Map<DFATransitionMapKey<State, Symbol>, State> transitionMap, State startState, Set<State> finalStates) {
        checkTransitionMapValid(states, alphabet, transitionMap);
        checkStartStateValid(states, startState);
        checkFinalStatesValid(states, finalStates);
        this.states = new HashSet<>(states);
        this.alphabet = new HashSet<>(alphabet);
        this.transitionMap = new HashMap<>(transitionMap);
        this.startState = startState;
        this.finalStates = finalStates;
    }

    private void checkTransitionMapValid(Set<State> states, Set<Symbol> alphabet, Map<DFATransitionMapKey<State, Symbol>, State> transitionMap) {
        Set<State> keyStates = transitionMap.keySet().stream().map(key -> key.state()).collect(Collectors.toSet());
        if (!states.containsAll(keyStates) || !states.containsAll(transitionMap.values())) {
            throw new IllegalArgumentException("Transition map contains state not defined in states set");
        }
        Set<Symbol> keySymbols = transitionMap.keySet().stream().map(key -> key.symbol()).collect(Collectors.toSet());
        if (!alphabet.containsAll(keySymbols)) {
            throw new IllegalArgumentException("Transition map contains symbol not defined in alphabet set");
        }
    }

    private void checkStartStateValid(Set<State> states, State startState) {
        if (!states.contains(startState)) {
            throw new IllegalArgumentException("Start state is not defined in states set");
        }
    }

    private void checkFinalStatesValid(Set<State> states, Set<State> finalStates) {
        if (!states.containsAll(finalStates)) {
            throw new IllegalArgumentException("Final states contains state not defined in states set");
        }
    }

    public Set<State> getStates() {
        return Collections.unmodifiableSet(states);
    }

    public Set<Symbol> getAlphabet() {
        return Collections.unmodifiableSet(alphabet);
    }

    public Map<DFATransitionMapKey<State, Symbol>, State> getTransitionMap() {
        return Collections.unmodifiableMap(transitionMap);
    }

    public State getStartState() {
        return startState;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public void addState(State state) {
        states.add(state);
    }

    public void addTransition(State from, State to, Symbol through) {
        checkStateIsValid(from);
        checkStateIsValid(to);
        checkSymbolIsValid(through);
        transitionMap.put(new DFATransitionMapKey<>(from, through), to);
    }

    private void checkStateIsValid(State state) {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State is not defined in states set.");
        }
    }

    private void checkSymbolIsValid(Symbol symbol) {
        if (!alphabet.contains(symbol)) {
            throw new IllegalArgumentException("Symbol is not defined in alphabet set.");
        }
    }

    @Override
    public String toString() {
        return "DFA[" +
                "states=" + states + ", " +
                "alphabet=" + alphabet + ", " +
                "transitionMap=" + transitionMap + ", " +
                "startState=" + startState + ", " +
                "finalStates=" + finalStates + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DFA<?, ?> dfa = (DFA<?, ?>) o;
        return Objects.equals(states, dfa.states)
                && Objects.equals(alphabet, dfa.alphabet)
                && Objects.equals(transitionMap, dfa.transitionMap)
                && Objects.equals(startState, dfa.startState)
                && Objects.equals(finalStates, dfa.finalStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(states, alphabet, transitionMap, startState, finalStates);
    }
}