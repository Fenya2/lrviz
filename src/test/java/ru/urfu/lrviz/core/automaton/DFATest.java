package ru.urfu.lrviz.core.automaton;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DFATest {
    @Test
    void testCreateDFAWithValidParametersExpectSuccess() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("0", "1");
        Map<AutomatonTransitionMapKey<String, String>, String> transitionMap = Map.of(
                new AutomatonTransitionMapKey<>("q0", "0"), "q0",
                new AutomatonTransitionMapKey<>("q0", "1"), "q1",
                new AutomatonTransitionMapKey<>("q1", "0"), "q2",
                new AutomatonTransitionMapKey<>("q1", "1"), "q0",
                new AutomatonTransitionMapKey<>("q2", "0"), "q1",
                new AutomatonTransitionMapKey<>("q2", "1"), "q2");
        String startState = "q0";
        Set<String> finalStates = Set.of("q2");

        DFA<String, String> DFA = new DFA<>(
                states, alphabet, transitionMap, startState, finalStates);

        assertNotNull(DFA);
        assertEquals(states, DFA.getStates());
        assertEquals(alphabet, DFA.getAlphabet());
        assertEquals(transitionMap, DFA.getTransitionMap());
        assertEquals(startState, DFA.getStartState());
        assertEquals(finalStates, DFA.getFinalStates());
    }

    @Test
    void testCreateDFAWithInvalidStateInGetTransitionMapExpectException() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a", "b");
        Map<AutomatonTransitionMapKey<String, String>, String> transitionMap = Map.of(
                new AutomatonTransitionMapKey<>("q0", "a"), "q1",
                new AutomatonTransitionMapKey<>("q2", "b"), "q0");// q2 нет в namedStates);
        String startState = "q0";
        Set<String> finalStates = Set.of("q1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates));
        assertTrue(exception.getMessage().contains("Transition map contains state not defined in namedStates set"));
    }

    @Test
    void testCreateDFAWithInvalidSymbolInGetTransitionMapExpectException() {
        Set<String> states = Set.of("s0", "s1");
        Set<String> alphabet = Set.of("x", "y");
        Map<AutomatonTransitionMapKey<String, String>, String> transitionMap = Map.of(
                new AutomatonTransitionMapKey<>("s0", "x"), "s1",
                new AutomatonTransitionMapKey<>("s1", "z"), "s0");
        String startState = "s0";
        Set<String> finalStates = Set.of("s1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates));
        assertTrue(exception.getMessage().contains("Transition map contains symbol not defined in alphabet set"));
    }

    @Test
    void testCreateDFAWithInvalidGetStartStateExpectException() {
        Set<String> states = Set.of("A", "B", "C");
        Set<String> alphabet = Set.of("0", "1");
        Map<AutomatonTransitionMapKey<String, String>, String> transitionMap = Map.of(
                new AutomatonTransitionMapKey<>("A", "0"), "B",
                new AutomatonTransitionMapKey<>("A", "1"), "C");
        String startState = "D"; // нет в namedStates
        Set<String> finalStates = Set.of("C");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates));
        assertTrue(exception.getMessage().contains("Start state is not defined in namedStates set"));
    }

    @Test
    void testCreateDFAWithInvalidFinalStateExpectException() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a");
        Map<AutomatonTransitionMapKey<String, String>, String> transitionMap = Map.of(
                new AutomatonTransitionMapKey<>("q0", "a"), "q1");
        String startState = "q0";
        Set<String> finalStates = new HashSet<>(Set.of("q1", "q2")); // q2 нет в namedStates

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates));
        assertTrue(exception.getMessage().contains("Final namedStates contains state not defined in namedStates set"));
    }

    @Test
    void testCreateDFAWithTransitionToInvalidStateExpectException() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a");
        Map<AutomatonTransitionMapKey<String, String>, String> transitionMap = Map.of(
                new AutomatonTransitionMapKey<>("q0", "a"), "q2");
        String startState = "q0";
        Set<String> finalStates = Set.of("q1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates)
        );
        assertTrue(exception.getMessage().contains("Transition map contains state not defined in namedStates set"));
    }
}