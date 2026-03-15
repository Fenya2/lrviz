package ru.urfu.lrviz.core.lr.lr1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.BuildContextCreator;
import ru.urfu.lrviz.core.lr.BuildOptions;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.urfu.lrviz.core.GrammarExamples.*;
import static ru.urfu.lrviz.core.lr.AutomatonType.LR_1;
import static ru.urfu.lrviz.core.lr.state.name.generation.ByTransitionSymbolStateNameGenerator.INIT_AUTOMATON_STATE_NAME;

@SuppressWarnings("java:S117") // Имена переменных здесь оправданы
@SpringBootTest
class LR1AutomatonBuilderTest {
    private final LR1AutomatonBuilder builder;
    private final BuildContextCreator contextCreator;

    @Autowired
    LR1AutomatonBuilderTest(LR1AutomatonBuilder builder, BuildContextCreator contextCreator) {
        this.builder = builder;
        this.contextCreator = contextCreator;
    }

    @Test
    void build1() {
        Grammar grammar = GrammarExamples.get(G_1);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal INT = new Terminal("i");
        Terminal REAL = new Terminal("r");
        Terminal SEMICOLON = new Terminal(";");
        Terminal a = new Terminal("a");

        NonTerminal D = new NonTerminal("D");
        NonTerminal T = new NonTerminal("T");
        NonTerminal L = new NonTerminal("L");

        Rule r1 = new Rule(D, T, L);
        Rule r2 = new Rule(T, REAL);
        Rule r3 = new Rule(T, INT);
        Rule r4 = new Rule(L, L, SEMICOLON, a);
        Rule r5 = new Rule(L, a);

        NonTerminal D_prime = new NonTerminal("D'");
        Rule r0 = new Rule(D_prime, D);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r2, 0, a),
                new LR1Item(r0, 0, EOF),
                new LR1Item(r1, 0, EOF),
                new LR1Item(r3, 0, a));

        LRState TState = new LRState(
                new LR1Item(r5, 0, EOF),
                new LR1Item(r5, 0, SEMICOLON),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r4, 0, SEMICOLON),
                new LR1Item(r1, 1, EOF));

        LRState realState = new LRState(new LR1Item(r2, 1, a));

        LRState intState = new LRState(new LR1Item(r3, 1, a));

        LRState a1State = new LRState(
                new LR1Item(r5, 1, EOF),
                new LR1Item(r5, 1, SEMICOLON));

        LRState LState = new LRState(
                new LR1Item(r1, 2, EOF),
                new LR1Item(r4, 1, EOF),
                new LR1Item(r4, 1, SEMICOLON));

        LRState semicolonState = new LRState(
                new LR1Item(r4, 2, EOF),
                new LR1Item(r4, 2, SEMICOLON));

        LRState a2State = new LRState(
                new LR1Item(r4, 3, EOF),
                new LR1Item(r4, 3, SEMICOLON));

        LRState DState = new LRState(new LR1Item(r0, 1, EOF));

        String TStateName = "T1";
        String RealStateName = "r1";
        String IntStateName = "i1";
        String A1StateName = "a1";
        String LStateName = "L1";
        String SemicolonStateName = ";1";
        String A2StateName = "a2";
        String DStateName = "D1";

        Map<String, LRState> states = Map.of(
                INIT_AUTOMATON_STATE_NAME, startState,
                TStateName, TState,
                RealStateName, realState,
                IntStateName, intState,
                A1StateName, a1State,
                LStateName, LState,
                SemicolonStateName, semicolonState,
                A2StateName, a2State,
                DStateName, DState);

        Map<LRAutomaton.TransitionKey, String> transitions = Map.of(
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, REAL), RealStateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, INT), IntStateName,
                new LRAutomaton.TransitionKey(SemicolonStateName, a), A2StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, D), DStateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, T), TStateName,
                new LRAutomaton.TransitionKey(TStateName, L), LStateName,
                new LRAutomaton.TransitionKey(TStateName, a), A1StateName,
                new LRAutomaton.TransitionKey(LStateName, SEMICOLON), SemicolonStateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }

    @Test
    void build2() {
        Grammar grammar = GrammarExamples.get(G_2);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");

        Rule r1 = new Rule(S, A, A);
        Rule r2 = new Rule(A, a, A);
        Rule r3 = new Rule(A, b);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r3, 0, a),
                new LR1Item(r3, 0, b),
                new LR1Item(r1, 0, EOF),
                new LR1Item(r2, 0, a),
                new LR1Item(r2, 0, b),
                new LR1Item(r0, 0, EOF));

        LRState b1State = new LRState(
                new LR1Item(r3, 1, a),
                new LR1Item(r3, 1, b));

        LRState a1State = new LRState(
                new LR1Item(r2, 1, a),
                new LR1Item(r2, 1, b),
                new LR1Item(r3, 0, a),
                new LR1Item(r3, 0, b),
                new LR1Item(r2, 0, a),
                new LR1Item(r2, 0, b));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        LRState A1State = new LRState(
                new LR1Item(r3, 0, EOF),
                new LR1Item(r2, 0, EOF),
                new LR1Item(r1, 1, EOF));

        LRState A3State = new LRState(
                new LR1Item(r2, 2, a),
                new LR1Item(r2, 2, b));

        LRState b2State = new LRState(new LR1Item(r3, 1, EOF));

        LRState a2State = new LRState(
                new LR1Item(r2, 1, EOF),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r2, 0, EOF));

        LRState A2State = new LRState(new LR1Item(r1, 2, EOF));

        LRState A4State = new LRState(new LR1Item(r2, 2, EOF));

        String B1StateName = "b1";
        String A1StateName = "a1";
        String S1StateName = "S1";
        String A1StateName2 = "A1";
        String A3StateName = "A3";
        String B2StateName = "b2";
        String A2StateName = "a2";
        String A2StateName2 = "A2";
        String A4StateName = "A4";

        Map<String, LRState> states = Map.of(
                INIT_AUTOMATON_STATE_NAME, startState,
                B1StateName, b1State,
                A1StateName, a1State,
                S1StateName, S1State,
                A1StateName2, A1State,
                A3StateName, A3State,
                B2StateName, b2State,
                A2StateName, a2State,
                A2StateName2, A2State,
                A4StateName, A4State);

        Map<LRAutomaton.TransitionKey, String> transitions = HashMap.newHashMap(13);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, a), A1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, A), A3StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, b), B1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, a), A1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, A), A1StateName2);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName2, b), B2StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName2, a), A2StateName);
        transitions.put(new LRAutomaton.TransitionKey(A2StateName, b), B2StateName);
        transitions.put(new LRAutomaton.TransitionKey(A2StateName, a), A2StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, b), B1StateName);
        transitions.put(new LRAutomaton.TransitionKey(A2StateName, A), A4StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName2, A), A2StateName2);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }

    @Test
    void build3() {
        Grammar grammar = GrammarExamples.get(G_3);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        NonTerminal B = new NonTerminal("B");
        NonTerminal C = new NonTerminal("C");

        Rule r1 = new Rule(S, S, LPAREN, A, RPAREN, S);
        Rule r2 = new Rule(S);
        Rule r3 = new Rule(A, B, C);
        Rule r4 = new Rule(A);
        Rule r5 = new Rule(B, b);
        Rule r6 = new Rule(C, c);
        Rule r7 = new Rule(C);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r2, 0, EOF),
                new LR1Item(r2, 0, LPAREN),
                new LR1Item(r1, 0, EOF),
                new LR1Item(r1, 0, LPAREN),
                new LR1Item(r0, 0, EOF));

        LRState LPAREN1State = new LRState(
                new LR1Item(r4, 0, RPAREN),
                new LR1Item(r3, 0, RPAREN),
                new LR1Item(r5, 0, c),
                new LR1Item(r5, 0, RPAREN),
                new LR1Item(r1, 2, EOF),
                new LR1Item(r1, 2, LPAREN));

        LRState b1State = new LRState(
                new LR1Item(r5, 1, c),
                new LR1Item(r5, 1, RPAREN));

        LRState C1State = new LRState(new LR1Item(r3, 2, RPAREN));

        LRState A1State = new LRState(
                new LR1Item(r1, 3, EOF),
                new LR1Item(r1, 3, LPAREN));

        LRState S2State = new LRState(
                new LR1Item(r1, 1, EOF),
                new LR1Item(r1, 1, LPAREN),
                new LR1Item(r1, 5, EOF),
                new LR1Item(r1, 5, LPAREN));

        LRState S1State = new LRState(
                new LR1Item(r0, 1, EOF),
                new LR1Item(r1, 1, EOF),
                new LR1Item(r1, 1, LPAREN));

        LRState RPAREN1State = new LRState(
                new LR1Item(r2, 0, EOF),
                new LR1Item(r2, 0, LPAREN),
                new LR1Item(r1, 0, EOF),
                new LR1Item(r1, 0, LPAREN),
                new LR1Item(r1, 4, EOF),
                new LR1Item(r1, 4, LPAREN));

        LRState c1State = new LRState(new LR1Item(r6, 1, RPAREN));

        LRState B1State = new LRState(
                new LR1Item(r3, 1, RPAREN),
                new LR1Item(r7, 0, RPAREN),
                new LR1Item(r6, 0, RPAREN));

        String LPAREN1StateName = "(1";
        String b1StateName = "b1";
        String C1StateName = "C1";
        String A1StateName = "A1";
        String S2StateName = "S2";
        String S1StateName = "S1";
        String RPAREN1StateName = ")1";
        String c1StateName = "c1";
        String B1StateName = "B1";

        Map<String, LRState> states = HashMap.newHashMap(11);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(LPAREN1StateName, LPAREN1State);
        states.put(b1StateName, b1State);
        states.put(C1StateName, C1State);
        states.put(A1StateName, A1State);
        states.put(S2StateName, S2State);
        states.put(S1StateName, S1State);
        states.put(RPAREN1StateName, RPAREN1State);
        states.put(c1StateName, c1State);
        states.put(B1StateName, B1State);

        Map<LRAutomaton.TransitionKey, String> transitions = HashMap.newHashMap(13);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, RPAREN), RPAREN1StateName);
        transitions.put(new LRAutomaton.TransitionKey(LPAREN1StateName, B), B1StateName);
        transitions.put(new LRAutomaton.TransitionKey(LPAREN1StateName, A), A1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, c), c1StateName);
        transitions.put(new LRAutomaton.TransitionKey(RPAREN1StateName, S), S2StateName);
        transitions.put(new LRAutomaton.TransitionKey(S1StateName, LPAREN), LPAREN1StateName);
        transitions.put(new LRAutomaton.TransitionKey(LPAREN1StateName, b), b1StateName);
        transitions.put(new LRAutomaton.TransitionKey(S2StateName, LPAREN), LPAREN1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, C), C1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }

    @Test
    void build4() {
        Grammar grammar = GrammarExamples.get(G_4);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal u = new Terminal("u");
        Terminal v = new Terminal("v");
        Terminal w = new Terminal("w");
        Terminal x = new Terminal("x");
        Terminal y = new Terminal("y");
        Terminal z = new Terminal("z");

        NonTerminal S = new NonTerminal("S");
        NonTerminal B = new NonTerminal("B");
        NonTerminal D = new NonTerminal("D");
        NonTerminal E = new NonTerminal("E");
        NonTerminal F = new NonTerminal("F");

        Rule r1 = new Rule(S, u, B, D, z);
        Rule r2 = new Rule(B, B, v);
        Rule r3 = new Rule(B, w);
        Rule r4 = new Rule(D, E, F);
        Rule r5 = new Rule(E, y);
        Rule r6 = new Rule(E);
        Rule r7 = new Rule(F, x);
        Rule r8 = new Rule(F);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r0, 0, EOF),
                new LR1Item(r1, 0, EOF));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        LRState u1State = new LRState(
                new LR1Item(r1, 1, EOF),
                new LR1Item(r2, 0, x),
                new LR1Item(r3, 0, x),
                new LR1Item(r2, 0, y),
                new LR1Item(r3, 0, y),
                new LR1Item(r2, 0, z),
                new LR1Item(r3, 0, z),
                new LR1Item(r2, 0, v),
                new LR1Item(r3, 0, v));

        LRState B1State = new LRState(
                new LR1Item(r4, 0, z),
                new LR1Item(r6, 0, x),
                new LR1Item(r6, 0, z),
                new LR1Item(r1, 2, EOF),
                new LR1Item(r2, 1, x),
                new LR1Item(r2, 1, y),
                new LR1Item(r2, 1, z),
                new LR1Item(r2, 1, v),
                new LR1Item(r5, 0, x),
                new LR1Item(r5, 0, z));

        LRState D1State = new LRState(new LR1Item(r1, 3, EOF));

        LRState z1State = new LRState(new LR1Item(r1, 4, EOF));

        LRState w1State = new LRState(
                new LR1Item(r3, 1, x),
                new LR1Item(r3, 1, y),
                new LR1Item(r3, 1, z),
                new LR1Item(r3, 1, v));

        LRState y1State = new LRState(
                new LR1Item(r5, 1, x),
                new LR1Item(r5, 1, z));

        LRState E1State = new LRState(
                new LR1Item(r7, 0, z),
                new LR1Item(r4, 1, z),
                new LR1Item(r8, 0, z));

        LRState x1State = new LRState(new LR1Item(r7, 1, z));

        LRState F1State = new LRState(new LR1Item(r4, 2, z));

        LRState v1State = new LRState(
                new LR1Item(r2, 2, x),
                new LR1Item(r2, 2, y),
                new LR1Item(r2, 2, z),
                new LR1Item(r2, 2, v));

        String S1StateName = "S1";
        String u1StateName = "u1";
        String B1StateName = "B1";
        String D1StateName = "D1";
        String z1StateName = "z1";
        String w1StateName = "w1";
        String y1StateName = "y1";
        String E1StateName = "E1";
        String x1StateName = "x1";
        String F1StateName = "F1";
        String v1StateName = "v1";

        Map<String, LRState> states = HashMap.newHashMap(13);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(S1StateName, S1State);
        states.put(u1StateName, u1State);
        states.put(B1StateName, B1State);
        states.put(D1StateName, D1State);
        states.put(z1StateName, z1State);
        states.put(w1StateName, w1State);
        states.put(y1StateName, y1State);
        states.put(E1StateName, E1State);
        states.put(x1StateName, x1State);
        states.put(F1StateName, F1State);
        states.put(v1StateName, v1State);

        Map<LRAutomaton.TransitionKey, String> transitions = HashMap.newHashMap(11);
        transitions.put(new LRAutomaton.TransitionKey(E1StateName, F), F1StateName);
        transitions.put(new LRAutomaton.TransitionKey(u1StateName, w), w1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, E), E1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, D), D1StateName);
        transitions.put(new LRAutomaton.TransitionKey(D1StateName, z), z1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, u), u1StateName);
        transitions.put(new LRAutomaton.TransitionKey(u1StateName, B), B1StateName);
        transitions.put(new LRAutomaton.TransitionKey(E1StateName, x), x1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, y), y1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, v), v1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }

    @Test
    void build5() {
        Grammar grammar = GrammarExamples.get(G_5);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");

        NonTerminal S = new NonTerminal("S");

        Rule r1 = new Rule(S, LPAREN, S, RPAREN);
        Rule r2 = new Rule(S);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r2, 0, EOF),
                new LR1Item(r0, 0, EOF),
                new LR1Item(r1, 0, EOF));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        LRState S2State = new LRState(new LR1Item(r1, 2, EOF));

        LRState S3State = new LRState(new LR1Item(r1, 2, RPAREN));

        LRState LPAREN1State = new LRState(
                new LR1Item(r2, 0, RPAREN),
                new LR1Item(r1, 0, RPAREN),
                new LR1Item(r1, 1, EOF));

        LRState LPAREN2State = new LRState(
                new LR1Item(r2, 0, RPAREN),
                new LR1Item(r1, 0, RPAREN),
                new LR1Item(r1, 1, RPAREN));

        LRState RPAREN1State = new LRState(new LR1Item(r1, 3, EOF));

        LRState RPAREN2State = new LRState(new LR1Item(r1, 3, RPAREN));

        String S1StateName = "S1";
        String S2StateName = "S2";
        String S3StateName = "S3";
        String LPAREN1StateName = "(1";
        String LPAREN2StateName = "(2";
        String RPAREN1StateName = ")1";
        String RPAREN2StateName = ")2";

        Map<String, LRState> states = HashMap.newHashMap(9);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(S1StateName, S1State);
        states.put(S2StateName, S2State);
        states.put(S3StateName, S3State);
        states.put(LPAREN1StateName, LPAREN1State);
        states.put(LPAREN2StateName, LPAREN2State);
        states.put(RPAREN1StateName, RPAREN1State);
        states.put(RPAREN2StateName, RPAREN2State);

        Map<LRAutomaton.TransitionKey, String> transitions = HashMap.newHashMap(10);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new LRAutomaton.TransitionKey(S2StateName, RPAREN), RPAREN1StateName);
        transitions.put(new LRAutomaton.TransitionKey(LPAREN2StateName, LPAREN), LPAREN2StateName);
        transitions.put(new LRAutomaton.TransitionKey(LPAREN1StateName, LPAREN), LPAREN2StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, LPAREN), LPAREN1StateName);
        transitions.put(new LRAutomaton.TransitionKey(S3StateName, RPAREN), RPAREN2StateName);
        transitions.put(new LRAutomaton.TransitionKey(LPAREN2StateName, S), S3StateName);
        transitions.put(new LRAutomaton.TransitionKey(LPAREN1StateName, S), S2StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }

    @Test
    void build6() {
        Grammar grammar = GrammarExamples.get(G_6);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Terminal d = new Terminal("d");

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");

        Rule r1 = new Rule(S, A, a);
        Rule r2 = new Rule(S, b, A, c);
        Rule r3 = new Rule(S, b, c);
        Rule r4 = new Rule(S, b, d, a);
        Rule r5 = new Rule(A, d);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r1, 0, EOF),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r0, 0, EOF),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r2, 0, EOF),
                new LR1Item(r5, 0, a));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        LRState A1State = new LRState(new LR1Item(r1, 1, EOF));

        LRState a1State = new LRState(new LR1Item(r1, 2, EOF));

        LRState b1State = new LRState(
                new LR1Item(r3, 1, EOF),
                new LR1Item(r4, 1, EOF),
                new LR1Item(r2, 1, EOF),
                new LR1Item(r5, 0, c));

        LRState d1State = new LRState(new LR1Item(r5, 1, a));

        LRState d2State = new LRState(
                new LR1Item(r5, 1, c),
                new LR1Item(r4, 2, EOF));

        LRState a2State = new LRState(new LR1Item(r4, 3, EOF));

        LRState A2State = new LRState(new LR1Item(r2, 2, EOF));

        LRState c1State = new LRState(new LR1Item(r3, 2, EOF));

        LRState c2State = new LRState(new LR1Item(r2, 3, EOF));

        String S1StateName = "S1";
        String A1StateName = "A1";
        String a1StateName = "a1";
        String b1StateName = "b1";
        String d1StateName = "d1";
        String d2StateName = "d2";
        String a2StateName = "a2";
        String A2StateName = "A2";
        String c1StateName = "c1";
        String c2StateName = "c2";

        Map<String, LRState> states = HashMap.newHashMap(12);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(S1StateName, S1State);
        states.put(A1StateName, A1State);
        states.put(a1StateName, a1State);
        states.put(b1StateName, b1State);
        states.put(d1StateName, d1State);
        states.put(d2StateName, d2State);
        states.put(a2StateName, a2State);
        states.put(A2StateName, A2State);
        states.put(c1StateName, c1State);
        states.put(c2StateName, c2State);

        Map<LRAutomaton.TransitionKey, String> transitions = HashMap.newHashMap(11);
        transitions.put(new LRAutomaton.TransitionKey(b1StateName, d), d2StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, b), b1StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, a), a1StateName);
        transitions.put(new LRAutomaton.TransitionKey(b1StateName, A), A2StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, d), d1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, A), A1StateName);
        transitions.put(new LRAutomaton.TransitionKey(d2StateName, a), a2StateName);
        transitions.put(new LRAutomaton.TransitionKey(A2StateName, c), c2StateName);
        transitions.put(new LRAutomaton.TransitionKey(b1StateName, c), c1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }

    @Test
    void build7() {
        Grammar grammar = GrammarExamples.get(G_7);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        NonTerminal B = new NonTerminal("B");
        NonTerminal C = new NonTerminal("C");

        Rule r1 = new Rule(S, A, B, C);
        Rule r2 = new Rule(A, A, a);
        Rule r3 = new Rule(A, a);
        Rule r4 = new Rule(B, B, b);
        Rule r5 = new Rule(B, b);
        Rule r6 = new Rule(C, C, c);
        Rule r7 = new Rule(C, c);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r2, 0, b),
                new LR1Item(r3, 0, b),
                new LR1Item(r2, 0, a),
                new LR1Item(r3, 0, a),
                new LR1Item(r0, 0, EOF),
                new LR1Item(r1, 0, EOF));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        LRState A1State = new LRState(
                new LR1Item(r2, 1, b),
                new LR1Item(r2, 1, a),
                new LR1Item(r4, 0, c),
                new LR1Item(r5, 0, c),
                new LR1Item(r4, 0, b),
                new LR1Item(r5, 0, b),
                new LR1Item(r1, 1, EOF));

        LRState a1State = new LRState(
                new LR1Item(r3, 1, b),
                new LR1Item(r3, 1, a));

        LRState a2State = new LRState(
                new LR1Item(r2, 2, b),
                new LR1Item(r2, 2, a));

        LRState B1State = new LRState(
                new LR1Item(r4, 1, c),
                new LR1Item(r4, 1, b),
                new LR1Item(r6, 0, EOF),
                new LR1Item(r7, 0, EOF),
                new LR1Item(r6, 0, c),
                new LR1Item(r7, 0, c),
                new LR1Item(r1, 2, EOF));

        LRState b1State = new LRState(
                new LR1Item(r5, 1, c),
                new LR1Item(r5, 1, b));

        LRState b2State = new LRState(
                new LR1Item(r4, 2, c),
                new LR1Item(r4, 2, b));

        LRState C1State = new LRState(
                new LR1Item(r6, 1, EOF),
                new LR1Item(r6, 1, c),
                new LR1Item(r1, 3, EOF));

        LRState c1State = new LRState(
                new LR1Item(r7, 1, EOF),
                new LR1Item(r7, 1, c));

        LRState c2State = new LRState(
                new LR1Item(r6, 2, EOF),
                new LR1Item(r6, 2, c));

        String S1StateName = "S1";
        String A1StateName = "A1";
        String a1StateName = "a1";
        String a2StateName = "a2";
        String B1StateName = "B1";
        String b1StateName = "b1";
        String b2StateName = "b2";
        String C1StateName = "C1";
        String c1StateName = "c1";
        String c2StateName = "c2";

        Map<String, LRState> states = HashMap.newHashMap(12);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(S1StateName, S1State);
        states.put(A1StateName, A1State);
        states.put(a1StateName, a1State);
        states.put(a2StateName, a2State);
        states.put(B1StateName, B1State);
        states.put(b1StateName, b1State);
        states.put(b2StateName, b2State);
        states.put(C1StateName, C1State);
        states.put(c1StateName, c1State);
        states.put(c2StateName, c2State);

        Map<LRAutomaton.TransitionKey, String> transitions = HashMap.newHashMap(11);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, a), a1StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, a), a2StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, b), b1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, b), b2StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, c), c1StateName);
        transitions.put(new LRAutomaton.TransitionKey(C1StateName, c), c2StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, A), A1StateName);
        transitions.put(new LRAutomaton.TransitionKey(A1StateName, B), B1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, C), C1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }

    @Test
    void build8() {
        Grammar grammar = GrammarExamples.get(G_8);
        LRAutomaton actual = builder.build(grammar, contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));

        Terminal i = new Terminal("i");
        Terminal plus = new Terminal("+");
        Terminal lParen = new Terminal("(");
        Terminal rParen = new Terminal(")");

        NonTerminal E = new NonTerminal("E");
        NonTerminal T = new NonTerminal("T");

        Rule r1 = new Rule(E, T);
        Rule r2 = new Rule(E, E, plus, T);
        Rule r3 = new Rule(T, i);
        Rule r4 = new Rule(T, lParen, E, rParen);

        NonTerminal EPrime = new NonTerminal("E'");
        Rule r0 = new Rule(EPrime, E);

        LookAheadSymbol EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r0, 0, EOF),
                new LR1Item(r1, 0, EOF),
                new LR1Item(r1, 0, plus),
                new LR1Item(r2, 0, EOF),
                new LR1Item(r2, 0, plus),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r3, 0, plus),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r4, 0, plus));

        LRState E3State = new LRState(
                new LR1Item(r2, 1, rParen),
                new LR1Item(r2, 1, plus),
                new LR1Item(r4, 2, rParen),
                new LR1Item(r4, 2, plus));

        LRState E2State = new LRState(
                new LR1Item(r2, 1, rParen),
                new LR1Item(r2, 1, plus),
                new LR1Item(r4, 2, EOF),
                new LR1Item(r4, 2, plus));

        LRState E1State = new LRState(
                new LR1Item(r0, 1, EOF),
                new LR1Item(r2, 1, EOF),
                new LR1Item(r2, 1, plus));

        LRState L2ParenState = new LRState(
                new LR1Item(r1, 0, rParen),
                new LR1Item(r1, 0, plus),
                new LR1Item(r2, 0, rParen),
                new LR1Item(r2, 0, plus),
                new LR1Item(r3, 0, rParen),
                new LR1Item(r3, 0, plus),
                new LR1Item(r4, 0, rParen),
                new LR1Item(r4, 0, plus),
                new LR1Item(r4, 1, rParen),
                new LR1Item(r4, 1, plus));

        LRState L1ParenState = new LRState(
                new LR1Item(r1, 0, rParen),
                new LR1Item(r1, 0, plus),
                new LR1Item(r2, 0, rParen),
                new LR1Item(r2, 0, plus),
                new LR1Item(r3, 0, rParen),
                new LR1Item(r3, 0, plus),
                new LR1Item(r4, 0, rParen),
                new LR1Item(r4, 0, plus),
                new LR1Item(r4, 1, EOF),
                new LR1Item(r4, 1, plus));

        LRState R2ParenState = new LRState(
                new LR1Item(r4, 3, rParen),
                new LR1Item(r4, 3, plus));

        LRState R1ParenState = new LRState(
                new LR1Item(r4, 3, EOF),
                new LR1Item(r4, 3, plus));

        LRState i2State = new LRState(
                new LR1Item(r3, 1, rParen),
                new LR1Item(r3, 1, plus));

        LRState i1State = new LRState(
                new LR1Item(r3, 1, EOF),
                new LR1Item(r3, 1, plus));

        LRState Plus2State = new LRState(
                new LR1Item(r2, 2, rParen),
                new LR1Item(r2, 2, plus),
                new LR1Item(r3, 0, rParen),
                new LR1Item(r3, 0, plus),
                new LR1Item(r4, 0, rParen),
                new LR1Item(r4, 0, plus));

        LRState Plus1State = new LRState(
                new LR1Item(r2, 2, EOF),
                new LR1Item(r2, 2, plus),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r3, 0, plus),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r4, 0, plus));

        LRState T4State = new LRState(
                new LR1Item(r2, 3, rParen),
                new LR1Item(r2, 3, plus));

        LRState T3State = new LRState(
                new LR1Item(r2, 3, EOF),
                new LR1Item(r2, 3, plus));

        LRState T2State = new LRState(
                new LR1Item(r1, 1, rParen),
                new LR1Item(r1, 1, plus));

        LRState T1State = new LRState(
                new LR1Item(r1, 1, EOF),
                new LR1Item(r1, 1, plus));

        String E3StateName = "E3";
        String E2StateName = "E2";
        String E1StateName = "E1";
        String L2StateName = "(2";
        String L1StateName = "(1";
        String R2StateName = ")2";
        String R1StateName = ")1";
        String i2StateName = "i2";
        String i1StateName = "i1";
        String P2StateName = "+2";
        String P1StateName = "+1";
        String T4StateName = "T4";
        String T3StateName = "T3";
        String T2StateName = "T2";
        String T1StateName = "T1";

        Map<String, LRState> states = HashMap.newHashMap(18);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(E3StateName, E3State);
        states.put(E2StateName, E2State);
        states.put(E1StateName, E1State);
        states.put(L2StateName, L2ParenState);
        states.put(L1StateName, L1ParenState);
        states.put(R2StateName, R2ParenState);
        states.put(R1StateName, R1ParenState);
        states.put(i2StateName, i2State);
        states.put(i1StateName, i1State);
        states.put(P2StateName, Plus2State);
        states.put(P1StateName, Plus1State);
        states.put(T4StateName, T4State);
        states.put(T3StateName, T3State);
        states.put(T2StateName, T2State);
        states.put(T1StateName, T1State);

        Map<LRAutomaton.TransitionKey, String> transitions = HashMap.newHashMap(27);
        transitions.put(new LRAutomaton.TransitionKey(E3StateName, plus), P2StateName);
        transitions.put(new LRAutomaton.TransitionKey(E3StateName, rParen), R2StateName);
        transitions.put(new LRAutomaton.TransitionKey(E2StateName, plus), P2StateName);
        transitions.put(new LRAutomaton.TransitionKey(E2StateName, rParen), R1StateName);
        transitions.put(new LRAutomaton.TransitionKey(E1StateName, plus), P1StateName);
        transitions.put(new LRAutomaton.TransitionKey(L2StateName, lParen), L2StateName);
        transitions.put(new LRAutomaton.TransitionKey(L2StateName, i), i2StateName);
        transitions.put(new LRAutomaton.TransitionKey(L2StateName, E), E3StateName);
        transitions.put(new LRAutomaton.TransitionKey(L2StateName, T), T2StateName);
        transitions.put(new LRAutomaton.TransitionKey(L1StateName, lParen), L2StateName);
        transitions.put(new LRAutomaton.TransitionKey(L1StateName, i), i2StateName);
        transitions.put(new LRAutomaton.TransitionKey(L1StateName, E), E2StateName);
        transitions.put(new LRAutomaton.TransitionKey(L1StateName, T), T2StateName);
        transitions.put(new LRAutomaton.TransitionKey(P2StateName, i), i2StateName);
        transitions.put(new LRAutomaton.TransitionKey(P2StateName, lParen), L2StateName);
        transitions.put(new LRAutomaton.TransitionKey(P2StateName, T), T4StateName);
        transitions.put(new LRAutomaton.TransitionKey(P1StateName, i), i1StateName);
        transitions.put(new LRAutomaton.TransitionKey(P1StateName, lParen), L1StateName);
        transitions.put(new LRAutomaton.TransitionKey(P1StateName, T), T3StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, i), i1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, lParen), L1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, E), E1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, T), T1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
    }
}