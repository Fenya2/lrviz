package ru.urfu.lrviz.core.lr.lrnew.lr0;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.lrnew.BuildContext;
import ru.urfu.lrviz.core.lr.lrnew.LRAutomaton;
import ru.urfu.lrviz.core.lr.lrnew.LRState;

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
     * L => L : a | a;
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
}