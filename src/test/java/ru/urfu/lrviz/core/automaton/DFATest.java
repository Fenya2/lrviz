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
        Map<DFATransitionMapKey<String, String>, String> transitionMap = Map.of(
                new DFATransitionMapKey<>("q0", "0"), "q0",
                new DFATransitionMapKey<>("q0", "1"), "q1",
                new DFATransitionMapKey<>("q1", "0"), "q2",
                new DFATransitionMapKey<>("q1", "1"), "q0",
                new DFATransitionMapKey<>("q2", "0"), "q1",
                new DFATransitionMapKey<>("q2", "1"), "q2");
        String startState = "q0";
        Set<String> finalStates = Set.of("q2");

        DFA<String, String> dfa = new DFA<>(
                states, alphabet, transitionMap, startState, finalStates);

        assertNotNull(dfa);
        assertEquals(states, dfa.getStates());
        assertEquals(alphabet, dfa.getAlphabet());
        assertEquals(transitionMap, dfa.getTransitionMap());
        assertEquals(startState, dfa.getStartState());
        assertEquals(finalStates, dfa.getFinalStates());
    }

    @Test
    void testCreateDFAWithInvalidStateInGetTransitionMapExpectException() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a", "b");
        Map<DFATransitionMapKey<String, String>, String> transitionMap = Map.of(
                new DFATransitionMapKey<>("q0", "a"), "q1",
                new DFATransitionMapKey<>("q2", "b"), "q0");// q2 нет в states);
        String startState = "q0";
        Set<String> finalStates = Set.of("q1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates));
        assertTrue(exception.getMessage().contains("Transition map contains state not defined in states set"));
    }

    @Test
    void testCreateDFAWithInvalidSymbolInGetTransitionMapExpectException() {
        Set<String> states = Set.of("s0", "s1");
        Set<String> alphabet = Set.of("x", "y");
        Map<DFATransitionMapKey<String, String>, String> transitionMap = Map.of(
                new DFATransitionMapKey<>("s0", "x"), "s1",
                new DFATransitionMapKey<>("s1", "z"), "s0");
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
        Map<DFATransitionMapKey<String, String>, String> transitionMap = Map.of(
                new DFATransitionMapKey<>("A", "0"), "B",
                new DFATransitionMapKey<>("A", "1"), "C");
        String startState = "D"; // нет в states
        Set<String> finalStates = Set.of("C");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates));
        assertTrue(exception.getMessage().contains("Start state is not defined in states set"));
    }

    @Test
    void testCreateDFAWithInvalidFinalStateExpectException() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a");
        Map<DFATransitionMapKey<String, String>, String> transitionMap = Map.of(
                new DFATransitionMapKey<>("q0", "a"), "q1");
        String startState = "q0";
        Set<String> finalStates = new HashSet<>(Set.of("q1", "q2")); // q2 нет в states

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates));
        assertTrue(exception.getMessage().contains("Final states contains state not defined in states set"));
    }

    @Test
    void testCreateDFAWithTransitionToInvalidStateExpectException() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a");
        Map<DFATransitionMapKey<String, String>, String> transitionMap = Map.of(
                new DFATransitionMapKey<>("q0", "a"), "q2");
        String startState = "q0";
        Set<String> finalStates = Set.of("q1");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DFA<>(states, alphabet, transitionMap, startState, finalStates)
        );
        assertTrue(exception.getMessage().contains("Transition map contains state not defined in states set"));
    }
}