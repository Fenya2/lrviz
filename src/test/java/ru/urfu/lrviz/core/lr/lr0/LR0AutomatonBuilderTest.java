package ru.urfu.lrviz.core.lr.lr0;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class LR0AutomatonBuilderTest {

    private static final String INIT_AUTOMATON_STATE_NAME = "∇";

    @Autowired
    private LR0AutomatonBuilder service;

    /**
     * D => T L;
     * T => int | real;
     * L => L ; a | a;
     */
    @Test
    void build1() {
        Terminal INT = new Terminal("int");
        Terminal REAL = new Terminal("real");
        Terminal SEMICOLON = new Terminal(";");
        Terminal a = new Terminal("a");
        Set<Terminal> terminals = Set.of(INT, REAL, SEMICOLON, a);

        NonTerminal D = new NonTerminal("D");
        NonTerminal T = new NonTerminal("T");
        NonTerminal L = new NonTerminal("L");
        Set<NonTerminal> nonTerminals = Set.of(D, T, L);

        Rule r1 = new Rule(D, T, L);
        Rule r2 = new Rule(T, REAL);
        Rule r3 = new Rule(T, INT);
        Rule r4 = new Rule(L, L, SEMICOLON, a);
        Rule r5 = new Rule(L, a);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, D);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal D_prime = new NonTerminal("D'");
        Rule r0 = new Rule(D_prime, D);

        LRState startState = new LRState(
                LR0Item.ofInitial(r0),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2),
                LR0Item.ofInitial(r3));
        LRState DState = new LRState(new LR0Item(r0, 1));
        LRState TState = new LRState(
                new LR0Item(r1, 1),
                LR0Item.ofInitial(r4),
                LR0Item.ofInitial(r5));
        LRState realState = new LRState(new LR0Item(r2, 1));
        LRState intState = new LRState(new LR0Item(r3, 1));
        LRState a1State = new LRState(new LR0Item(r5, 1));
        LRState LState = new LRState(
                new LR0Item(r4, 1),
                new LR0Item(r1, 2));
        LRState colonState = new LRState(new LR0Item(r4, 2));
        LRState a2State = new LRState(new LR0Item(r4, 3));

        String DStateName = "D1";
        String TStateName = "T1";
        String RealStateName = "real1";
        String IntStateName = "int1";
        String A1StateName = "a1";
        String LStateName = "L1";
        String SemicolonStateName = ";1";
        String A2StateName = "a2";
        Map<String, LRState> states = Map.of(
                INIT_AUTOMATON_STATE_NAME, startState,
                DStateName, DState,
                TStateName, TState,
                RealStateName, realState,
                IntStateName, intState,
                A1StateName, a1State,
                LStateName, LState,
                SemicolonStateName, colonState,
                A2StateName, a2State);

        Map<LRAutomaton.TransitionKey, String> transitions = Map.of(
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, D), DStateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, T), TStateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, REAL), RealStateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, INT), IntStateName,
                new LRAutomaton.TransitionKey(TStateName, L), LStateName,
                new LRAutomaton.TransitionKey(TStateName, a), A1StateName,
                new LRAutomaton.TransitionKey(LStateName, SEMICOLON), SemicolonStateName,
                new LRAutomaton.TransitionKey(SemicolonStateName, a), A2StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * S => A A;
     * A => a A | b;
     */
    @Test
    void build2() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Set<Terminal> terminals = Set.of(a, b);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        Set<NonTerminal> nonTerminals = Set.of(S, A);

        Rule r1 = new Rule(S, A, A);
        Rule r2 = new Rule(A, a, A);
        Rule r3 = new Rule(A, b);
        Set<Rule> rules = Set.of(r1, r2, r3);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, S);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LR0Item r3Item = LR0Item.ofInitial(r3);

        LRState startState = new LRState(LR0Item.ofInitial(r0), LR0Item.ofInitial(r1), LR0Item.ofInitial(r2), r3Item);
        LRState SState = new LRState(new LR0Item(r0, 1));
        LRState bState = new LRState(new LR0Item(r3, 1));
        LRState A1State = new LRState(new LR0Item(r1, 1), LR0Item.ofInitial(r2), r3Item);
        LRState aState = new LRState(new LR0Item(r2, 1), LR0Item.ofInitial(r2), r3Item);
        LRState A3State = new LRState(new LR0Item(r2, 2));
        LRState A2State = new LRState(new LR0Item(r1, 2));

        String SStateName = "S1";
        String A1StateName = "A1";
        String A2StateName = "A2";
        String A3StateName = "A3";
        String aStateName = "a1";
        String bStateName = "b1";

        Map<String, LRState> states = Map.of(
                INIT_AUTOMATON_STATE_NAME, startState,
                SStateName, SState,
                A1StateName, A1State,
                A2StateName, A2State,
                A3StateName, A3State,
                aStateName, aState,
                bStateName, bState);

        Map<LRAutomaton.TransitionKey, String> transitions = Map.of(
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), SStateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, A), A1StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, a), aStateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, b), bStateName,
                new LRAutomaton.TransitionKey(A1StateName, A), A2StateName,
                new LRAutomaton.TransitionKey(A1StateName, a), aStateName,
                new LRAutomaton.TransitionKey(A1StateName, b), bStateName,
                new LRAutomaton.TransitionKey(aStateName, A), A3StateName,
                new LRAutomaton.TransitionKey(aStateName, a), aStateName,
                new LRAutomaton.TransitionKey(aStateName, b), bStateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);

        Assertions.assertEquals(expected, actual);
    }

    /**
     * S => S ( A ) S | ;
     * A => B C | ;
     * B => b;
     * C => c | ;
     */
    @Test
    void build3() {
        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Set<Terminal> terminals = Set.of(LPAREN, RPAREN, b, c);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        NonTerminal B = new NonTerminal("B");
        NonTerminal C = new NonTerminal("C");
        Set<NonTerminal> nonTerminals = Set.of(S, A, B, C);

        Rule r1 = new Rule(S, S, LPAREN, A, RPAREN, S);
        Rule r2 = new Rule(S);
        Rule r3 = new Rule(A, B, C);
        Rule r4 = new Rule(A);
        Rule r5 = new Rule(B, b);
        Rule r6 = new Rule(C, c);
        Rule r7 = new Rule(C);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5, r6, r7);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, S);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LRState startState = new LRState(
                LR0Item.ofInitial(r0),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2));
        LRState S1State = new LRState(
                new LR0Item(r0, 1),
                new LR0Item(r1, 1));
        LRState LParenState = new LRState(
                new LR0Item(r1, 2),
                LR0Item.ofInitial(r3),
                LR0Item.ofInitial(r4),
                LR0Item.ofInitial(r5));
        LRState A1State = new LRState(new LR0Item(r1, 3));
        LRState RParenState = new LRState(
                new LR0Item(r1, 4),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2));
        LRState S2State = new LRState(
                new LR0Item(r1, 5),
                new LR0Item(r1, 1));
        LRState B1State = new LRState(
                new LR0Item(r3, 1),
                LR0Item.ofInitial(r6),
                LR0Item.ofInitial(r7));
        LRState b1State = new LRState(new LR0Item(r5, 1));
        LRState C1State = new LRState(new LR0Item(r3, 2));
        LRState c1State = new LRState(new LR0Item(r6, 1));

        // Имена состояний
        String S1StateName = "S1";
        String LParenStateName = "(1";
        String A1StateName = "A1";
        String RParenStateName = ")1";
        String S2StateName = "S2";
        String B1StateName = "B1";
        String b1StateName = "b1";
        String C1StateName = "C1";
        String c1StateName = "c1";

        Map<String, LRState> states = Map.of(
                INIT_AUTOMATON_STATE_NAME, startState,
                S1StateName, S1State,
                LParenStateName, LParenState,
                A1StateName, A1State,
                RParenStateName, RParenState,
                S2StateName, S2State,
                B1StateName, B1State,
                b1StateName, b1State,
                C1StateName, C1State,
                c1StateName, c1State);

        Map<LRAutomaton.TransitionKey, String> transitions = Map.of(
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName,
                new LRAutomaton.TransitionKey(S1StateName, LPAREN), LParenStateName,
                new LRAutomaton.TransitionKey(LParenStateName, A), A1StateName,
                new LRAutomaton.TransitionKey(LParenStateName, B), B1StateName,
                new LRAutomaton.TransitionKey(LParenStateName, b), b1StateName,
                new LRAutomaton.TransitionKey(A1StateName, RPAREN), RParenStateName,
                new LRAutomaton.TransitionKey(RParenStateName, S), S2StateName,
                new LRAutomaton.TransitionKey(S2StateName, LPAREN), LParenStateName,
                new LRAutomaton.TransitionKey(B1StateName, C), C1StateName,
                new LRAutomaton.TransitionKey(B1StateName, c), c1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);

        Assertions.assertEquals(expected, actual);
    }

    /**
     * S => u B D z;
     * B => B v | w;
     * D => E F;
     * E => y | ;
     * F => x | ;
     */
    @Test
    void build4() {
        Terminal v = new Terminal("v");
        Terminal u = new Terminal("u");
        Terminal w = new Terminal("w");
        Terminal x = new Terminal("x");
        Terminal y = new Terminal("y");
        Terminal z = new Terminal("z");
        Set<Terminal> terminals = Set.of(v, u, w, x, y, z);

        NonTerminal S = new NonTerminal("S");
        NonTerminal B = new NonTerminal("B");
        NonTerminal D = new NonTerminal("D");
        NonTerminal E = new NonTerminal("E");
        NonTerminal F = new NonTerminal("F");
        Set<NonTerminal> nonTerminals = Set.of(S, B, D, E, F);

        Rule r1 = new Rule(S, u, B, D, z);
        Rule r2 = new Rule(B, B, v);
        Rule r3 = new Rule(B, w);
        Rule r4 = new Rule(D, E, F);
        Rule r5 = new Rule(E, y);
        Rule r6 = new Rule(E);
        Rule r7 = new Rule(F, x);
        Rule r8 = Rule.ofEmpty(F);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5, r6, r7, r8);
        Grammar grammar = new Grammar(terminals, nonTerminals, rules, S);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LRState startState = new LRState(
                LR0Item.ofInitial(r0),
                LR0Item.ofInitial(r1));

        LRState S1State = new LRState(new LR0Item(r0, 1));

        LRState u1State = new LRState(
                new LR0Item(r1, 1),
                LR0Item.ofInitial(r2),
                LR0Item.ofInitial(r3));

        LRState B1State = new LRState(
                new LR0Item(r1, 2),
                new LR0Item(r2, 1),
                LR0Item.ofInitial(r4),
                LR0Item.ofInitial(r6),
                LR0Item.ofInitial(r5));

        LRState v1State = new LRState(new LR0Item(r2, 2));
        LRState w1State = new LRState(new LR0Item(r3, 1));

        LRState E1State = new LRState(
                new LR0Item(r4, 1),
                LR0Item.ofInitial(r7),
                LR0Item.ofInitial(r8));

        LRState y1State = new LRState(new LR0Item(r5, 1));
        LRState x1State = new LRState(new LR0Item(r7, 1));

        LRState F1State = new LRState(new LR0Item(r4, 2));
        LRState D1State = new LRState(new LR0Item(r1, 3));
        LRState z1State = new LRState(new LR0Item(r1, 4));

        String S1StateName = "S1";
        String u1StateName = "u1";
        String B1StateName = "B1";
        String v1StateName = "v1";
        String w1StateName = "w1";
        String E1StateName = "E1";
        String y1StateName = "y1";
        String x1StateName = "x1";
        String F1StateName = "F1";
        String D1StateName = "D1";
        String z1StateName = "z1";


        Map<String, LRState> states = new HashMap<>(12);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(S1StateName, S1State);
        states.put(u1StateName, u1State);
        states.put(B1StateName, B1State);
        states.put(v1StateName, v1State);
        states.put(w1StateName, w1State);
        states.put(E1StateName, E1State);
        states.put(y1StateName, y1State);
        states.put(x1StateName, x1State);
        states.put(F1StateName, F1State);
        states.put(D1StateName, D1State);
        states.put(z1StateName, z1State);

        Map<LRAutomaton.TransitionKey, String> transitions = new HashMap<>(11);

        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, u), u1StateName);
        transitions.put(new LRAutomaton.TransitionKey(u1StateName, B), B1StateName);
        transitions.put(new LRAutomaton.TransitionKey(u1StateName, w), w1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, v), v1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, E), E1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, D), D1StateName);
        transitions.put(new LRAutomaton.TransitionKey(B1StateName, y), y1StateName);
        transitions.put(new LRAutomaton.TransitionKey(E1StateName, F), F1StateName);
        transitions.put(new LRAutomaton.TransitionKey(E1StateName, x), x1StateName);
        transitions.put(new LRAutomaton.TransitionKey(D1StateName, z), z1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * S => ( S );
     * S => ;
     */
    @Test
    void build5() {
        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        Set<Terminal> terminals = Set.of(LPAREN, RPAREN);

        NonTerminal S = new NonTerminal("S");
        Set<NonTerminal> nonTerminals = Collections.singleton(S);

        Rule r1 = new Rule(S, LPAREN, S, RPAREN);
        Rule r2 = new Rule(S);
        Set<Rule> rules = Set.of(r1, r2);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, S);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LRState startState = new LRState(
                LR0Item.ofInitial(r0),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2));

        LRState S1State = new LRState(new LR0Item(r0, 1));

        LRState lParenState = new LRState(
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2),
                new LR0Item(r1, 1));

        LRState S2State = new LRState(new LR0Item(r1, 2));

        LRState rParenState = new LRState(new LR0Item(r1, 3));

        String S1StateName = "S1";
        String lParenStateName = "(1";
        String S2StateName = "S2";
        String rParenStateName = ")1";

        Map<String, LRState> states = Map.of(
                INIT_AUTOMATON_STATE_NAME, startState,
                S1StateName, S1State,
                lParenStateName, lParenState,
                S2StateName, S2State,
                rParenStateName, rParenState);

        Map<LRAutomaton.TransitionKey, String> transitions = Map.of(
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, LPAREN), lParenStateName,
                new LRAutomaton.TransitionKey(lParenStateName, S), S2StateName,
                new LRAutomaton.TransitionKey(lParenStateName, LPAREN), lParenStateName,
                new LRAutomaton.TransitionKey(S2StateName, RPAREN), rParenStateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);

        Assertions.assertEquals(expected, actual);
    }

    /**
     * S => A a | b A c | b c | b d a;
     * A => d;
     */
    @Test
    void build6() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Terminal d = new Terminal("d");
        Set<Terminal> terminals = Set.of(a, b, c, d);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        Set<NonTerminal> nonTerminals = Set.of(S, A);

        Rule r1 = new Rule(S, A, a);
        Rule r2 = new Rule(S, b, A, c);
        Rule r3 = new Rule(S, b, c);
        Rule r4 = new Rule(S, b, d, a);
        Rule r5 = new Rule(A, d);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, S);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LRState startState = new LRState(
                LR0Item.ofInitial(r0),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2),
                LR0Item.ofInitial(r3),
                LR0Item.ofInitial(r4),
                LR0Item.ofInitial(r5));

        LRState S1State = new LRState(new LR0Item(r0, 1));

        LRState d1State = new LRState(new LR0Item(r5, 1));

        LRState A1State = new LRState(new LR0Item(r1, 1));

        LRState a1State = new LRState(new LR0Item(r1, 2));

        LRState b1State = new LRState(
                new LR0Item(r2, 1),
                new LR0Item(r3, 1),
                new LR0Item(r4, 1),
                LR0Item.ofInitial(r5));

        LRState A2State = new LRState(new LR0Item(r2, 2));

        LRState c1State = new LRState(new LR0Item(r3, 2));

        LRState d2State = new LRState(
                new LR0Item(r5, 1),
                new LR0Item(r4, 2));

        LRState c2State = new LRState(new LR0Item(r2, 3));

        LRState a2State = new LRState(new LR0Item(r4, 3));

        String S1StateName = "S1";
        String d1StateName = "d1";
        String A1StateName = "A1";
        String a1StateName = "a1";
        String b1StateName = "b1";
        String A2StateName = "A2";
        String c1StateName = "c1";
        String d2StateName = "d2";
        String c2StateName = "c2";
        String a2StateName = "a2";

        Map<String, LRState> states = new HashMap<>(11);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(S1StateName, S1State);
        states.put(d1StateName, d1State);
        states.put(A1StateName, A1State);
        states.put(a1StateName, a1State);
        states.put(b1StateName, b1State);
        states.put(A2StateName, A2State);
        states.put(c1StateName, c1State);
        states.put(d2StateName, d2State);
        states.put(c2StateName, c2State);
        states.put(a2StateName, a2State);

        Map<LRAutomaton.TransitionKey, String> transitions = Map.of(
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, A), A1StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, b), b1StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, d), d1StateName,
                new LRAutomaton.TransitionKey(A1StateName, a), a1StateName,
                new LRAutomaton.TransitionKey(b1StateName, A), A2StateName,
                new LRAutomaton.TransitionKey(b1StateName, c), c1StateName,
                new LRAutomaton.TransitionKey(b1StateName, d), d2StateName,
                new LRAutomaton.TransitionKey(A2StateName, c), c2StateName,
                new LRAutomaton.TransitionKey(d2StateName, a), a2StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * S => A B C;
     * A => A a | a;
     * B => B b | b;
     * C => C c | c;
     */
    @Test
    void build7() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Set<Terminal> terminals = Set.of(a, b, c);

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        NonTerminal B = new NonTerminal("B");
        NonTerminal C = new NonTerminal("C");
        Set<NonTerminal> nonTerminals = Set.of(S, A, B, C);

        Rule r1 = new Rule(S, A, B, C);
        Rule r2 = new Rule(A, A, a);
        Rule r3 = new Rule(A, a);
        Rule r4 = new Rule(B, B, b);
        Rule r5 = new Rule(B, b);
        Rule r6 = new Rule(C, C, c);
        Rule r7 = new Rule(C, c);
        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5, r6, r7);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, S);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        LRState startState = new LRState(
                LR0Item.ofInitial(r0),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2),
                LR0Item.ofInitial(r3));

        LRState S1State = new LRState(new LR0Item(r0, 1));

        LRState a1State = new LRState(new LR0Item(r3, 1));

        LRState A1State = new LRState(
                new LR0Item(r2, 1),
                LR0Item.ofInitial(r4),
                LR0Item.ofInitial(r5),
                new LR0Item(r1, 1));

        LRState a2State = new LRState(new LR0Item(r2, 2));

        LRState b1State = new LRState(new LR0Item(r5, 1));

        LRState B1State = new LRState(
                new LR0Item(r4, 1),
                LR0Item.ofInitial(r6),
                LR0Item.ofInitial(r7),
                new LR0Item(r1, 2));

        LRState b2State = new LRState(new LR0Item(r4, 2));

        LRState c1State = new LRState(new LR0Item(r7, 1));

        LRState C1State = new LRState(
                new LR0Item(r6, 1),
                new LR0Item(r1, 3));

        LRState c2State = new LRState(new LR0Item(r6, 2));

        String S1StateName = "S1";
        String a1StateName = "a1";
        String A1StateName = "A1";
        String a2StateName = "a2";
        String b1StateName = "b1";
        String B1StateName = "B1";
        String b2StateName = "b2";
        String c1StateName = "c1";
        String C1StateName = "C1";
        String c2StateName = "c2";

        Map<String, LRState> states = new HashMap<>(11);

        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(S1StateName, S1State);
        states.put(a1StateName, a1State);
        states.put(A1StateName, A1State);
        states.put(a2StateName, a2State);
        states.put(b1StateName, b1State);
        states.put(B1StateName, B1State);
        states.put(b2StateName, b2State);
        states.put(c1StateName, c1State);
        states.put(C1StateName, C1State);
        states.put(c2StateName, c2State);

        Map<LRAutomaton.TransitionKey, String> transitions = Map.of(
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, A), A1StateName,
                new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, a), a1StateName,
                new LRAutomaton.TransitionKey(A1StateName, a), a2StateName,
                new LRAutomaton.TransitionKey(A1StateName, B), B1StateName,
                new LRAutomaton.TransitionKey(A1StateName, b), b1StateName,
                new LRAutomaton.TransitionKey(B1StateName, b), b2StateName,
                new LRAutomaton.TransitionKey(B1StateName, C), C1StateName,
                new LRAutomaton.TransitionKey(B1StateName, c), c1StateName,
                new LRAutomaton.TransitionKey(C1StateName, c), c2StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);

        Assertions.assertEquals(expected, actual);
    }

    /**
     * E => T | E + T;
     * T => i | ( E );
     */
    @Test
    void build8() {
        Terminal i = new Terminal("i");
        Terminal plus = new Terminal("+");
        Terminal lParen = new Terminal("(");
        Terminal rParen = new Terminal(")");
        Set<Terminal> terminals = Set.of(i, plus, lParen, rParen);

        NonTerminal E = new NonTerminal("E");
        NonTerminal T = new NonTerminal("T");
        Set<NonTerminal> nonTerminals = Set.of(E, T);

        Rule r1 = new Rule(E, T);
        Rule r2 = new Rule(E, E, plus, T);
        Rule r3 = new Rule(T, i);
        Rule r4 = new Rule(T, lParen, E, rParen);
        Set<Rule> rules = Set.of(r1, r2, r3, r4);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, E);

        BuildContext context = BuildContext.create();
        LRAutomaton actual = service.build(grammar, context);

        NonTerminal E_prime = new NonTerminal("E'");
        Rule r0 = new Rule(E_prime, E);

        LRState startState = new LRState(
                LR0Item.ofInitial(r0),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2),
                LR0Item.ofInitial(r3),
                LR0Item.ofInitial(r4));

        LRState plus1State = new LRState(
                new LR0Item(r2, 2),
                LR0Item.ofInitial(r3),
                LR0Item.ofInitial(r4));

        LRState E2State = new LRState(
                new LR0Item(r2, 1),
                new LR0Item(r4, 2));

        LRState E1State = new LRState(
                new LR0Item(r2, 1),
                LR0Item.ofFinal(r0));

        LRState rParen1State = new LRState(LR0Item.ofFinal(r4));

        LRState i1State = new LRState(LR0Item.ofFinal(r3));

        LRState T1State = new LRState(LR0Item.ofFinal(r1));

        LRState T2State = new LRState(LR0Item.ofFinal(r2));

        LRState lParen1State = new LRState(
                new LR0Item(r4, 1),
                LR0Item.ofInitial(r1),
                LR0Item.ofInitial(r2),
                LR0Item.ofInitial(r3),
                LR0Item.ofInitial(r4));

        String plus1StateName = "+1";
        String E1StateName = "E1";
        String E2StateName = "E2";
        String lParen1StateName = "(1";
        String i1StateName = "i1";
        String T1StateName = "T1";
        String T2StateName = "T2";
        String rParen1StateName = ")1";

        Map<String, LRState> states = new HashMap<>(9);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(plus1StateName, plus1State);
        states.put(E1StateName, E1State);
        states.put(E2StateName, E2State);
        states.put(rParen1StateName, rParen1State);
        states.put(i1StateName, i1State);
        states.put(T1StateName, T1State);
        states.put(T2StateName, T2State);
        states.put(lParen1StateName, lParen1State);

        Map<LRAutomaton.TransitionKey, String> transitions = new HashMap<>(14);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, i), i1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, E), E1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, lParen), lParen1StateName);
        transitions.put(new LRAutomaton.TransitionKey(INIT_AUTOMATON_STATE_NAME, T), T1StateName);
        transitions.put(new LRAutomaton.TransitionKey(E1StateName, plus), plus1StateName);
        transitions.put(new LRAutomaton.TransitionKey(lParen1StateName, lParen), lParen1StateName);
        transitions.put(new LRAutomaton.TransitionKey(lParen1StateName, i), i1StateName);
        transitions.put(new LRAutomaton.TransitionKey(lParen1StateName, E), E2StateName);
        transitions.put(new LRAutomaton.TransitionKey(lParen1StateName, T), T1StateName);
        transitions.put(new LRAutomaton.TransitionKey(plus1StateName, i), i1StateName);
        transitions.put(new LRAutomaton.TransitionKey(plus1StateName, T), T2StateName);
        transitions.put(new LRAutomaton.TransitionKey(plus1StateName, lParen), lParen1StateName);
        transitions.put(new LRAutomaton.TransitionKey(E2StateName, plus), plus1StateName);
        transitions.put(new LRAutomaton.TransitionKey(E2StateName, rParen), rParen1StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        Assertions.assertEquals(expected, actual);
    }
}