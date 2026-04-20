package ru.urfu.lrviz.core.lr.lalr1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.*;
import ru.urfu.lrviz.core.lr.lr1.EndOfChainSymbol;
import ru.urfu.lrviz.core.lr.lr1.LR1AutomatonBuilder;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.urfu.lrviz.core.GrammarExamples.*;
import static ru.urfu.lrviz.core.lr.AutomatonType.LR_1;
import static ru.urfu.lrviz.core.lr.state.name.generation.ByTransitionSymbolStateNameGenerator.INIT_AUTOMATON_STATE_NAME;

/**
 * @author fenya
 * @since 14.03.2026
 */
@SuppressWarnings("java:S117") // имена переменных здесь оправданы
@SpringBootTest
class ClassicLALR1AutomatonBuilderTest {
    private final LR1AutomatonBuilder lr1AutomatonBuilder;
    private final BuildContextCreator contextCreator;
    private final ClassicLALR1AutomatonBuilder builder;

    @Autowired
    private LRAutomatonReconstructor reconstructor;

    @Autowired
    ClassicLALR1AutomatonBuilderTest(LR1AutomatonBuilder lr1AutomatonBuilder, BuildContextCreator contextCreator, ClassicLALR1AutomatonBuilder builder) {
        this.lr1AutomatonBuilder = lr1AutomatonBuilder;
        this.contextCreator = contextCreator;
        this.builder = builder;
    }

    @ParameterizedTest
    @ValueSource(strings = {G_1, G_3, G_4, G_6, G_7})
    void buildWhereLR1EqualsLALR1(String grammarName) {
        Grammar grammar = GrammarExamples.get(grammarName);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        LRAutomaton expected = lr1AutomatonBuilder.build(grammar, context);
        LRAutomaton actual = builder.build(expected, context);
        assertEquals(expected, actual);
        assertEquals(expected, reconstructor.reconstruct(context.getBuildLog()));
    }

    @Test
    void build2() {
        Grammar grammar = GrammarExamples.get(G_2);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        LRAutomaton lr1Automaton = lr1AutomatonBuilder.build(grammar, context);
        LRAutomaton actual = builder.build(lr1Automaton, context);

        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");

        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");

        Rule r1 = new Rule(S, A, A);
        Rule r2 = new Rule(A, a, A);
        Rule r3 = new Rule(A, b);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        var EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r0, 0, EOF),
                new LR1Item(r1, 0, EOF),
                new LR1Item(r2, 0, a),
                new LR1Item(r2, 0, b),
                new LR1Item(r3, 0, a),
                new LR1Item(r3, 0, b));

        LRState aState = new LRState(
                new LR1Item(r2, 1, a),
                new LR1Item(r2, 1, EOF),
                new LR1Item(r2, 1, b),
                new LR1Item(r3, 0, a),
                new LR1Item(r3, 0, b),
                new LR1Item(r2, 0, a),
                new LR1Item(r2, 0, b),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r2, 0, EOF));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        LRState A1State = new LRState(
                new LR1Item(r1, 1, EOF),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r2, 0, EOF));

        LRState A2State = new LRState(new LR1Item(r1, 2, EOF));

        LRState A3A4State = new LRState(
                new LR1Item(r2, 2, a),
                new LR1Item(r2, 2, b),
                new LR1Item(r2, 2, EOF));

        LRState bState = new LRState(
                new LR1Item(r3, 1, a),
                new LR1Item(r3, 1, b),
                new LR1Item(r3, 1, EOF));

        String aStateName = "a1;a2";
        String S1StateName = "S1";
        String A1StateName = "A1";
        String A2StateName = "A2";
        String A3A4StateName = "A3;A4";
        String bStateName = "b1;b2";

        Map<String, LRState> states = HashMap.newHashMap(7);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(aStateName, aState);
        states.put(S1StateName, S1State);
        states.put(A1StateName, A1State);
        states.put(A2StateName, A2State);
        states.put(A3A4StateName, A3A4State);
        states.put(bStateName, bState);

        Map<TransitionKey, String> transitions = HashMap.newHashMap(11);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, a), aStateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, b), bStateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, A), A1StateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new TransitionKey(aStateName, a), aStateName);
        transitions.put(new TransitionKey(aStateName, b), bStateName);
        transitions.put(new TransitionKey(aStateName, A), A3A4StateName);
        transitions.put(new TransitionKey(A1StateName, a), aStateName);
        transitions.put(new TransitionKey(A1StateName, b), bStateName);
        transitions.put(new TransitionKey(A1StateName, A), A2StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
        assertEquals(expected, reconstructor.reconstruct(context.getBuildLog()));
    }

    @Test
    void build5() {
        Grammar grammar = GrammarExamples.get(G_5);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        LRAutomaton lr1Automaton = lr1AutomatonBuilder.build(grammar, context);
        LRAutomaton actual = builder.build(lr1Automaton, context);

        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");

        NonTerminal S = new NonTerminal("S");

        Rule r1 = new Rule(S, LPAREN, S, RPAREN);
        Rule r2 = new Rule(S);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        var EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r0, 0, EOF),
                new LR1Item(r2, 0, EOF),
                new LR1Item(r1, 0, EOF));

        LRState LPARENState = new LRState(
                new LR1Item(r2, 0, RPAREN),
                new LR1Item(r1, 0, RPAREN),
                new LR1Item(r1, 1, RPAREN),
                new LR1Item(r1, 1, EOF));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        LRState S2State = new LRState(
                new LR1Item(r1, 2, EOF),
                new LR1Item(r1, 2, RPAREN));

        LRState RPARENState = new LRState(
                new LR1Item(r1, 3, RPAREN),
                new LR1Item(r1, 3, EOF));

        String LPARENStateName = "(1;(2";
        String S1StateName = "S1";
        String S2StateName = "S2;S3";
        String RPARENStateName = ")1;)2";

        Map<String, LRState> states = HashMap.newHashMap(5);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(LPARENStateName, LPARENState);
        states.put(S1StateName, S1State);
        states.put(S2StateName, S2State);
        states.put(RPARENStateName, RPARENState);

        Map<TransitionKey, String> transitions = HashMap.newHashMap(5);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, LPAREN), LPARENStateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new TransitionKey(LPARENStateName, LPAREN), LPARENStateName);
        transitions.put(new TransitionKey(LPARENStateName, S), S2StateName);
        transitions.put(new TransitionKey(S2StateName, RPAREN), RPARENStateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
        assertEquals(expected, reconstructor.reconstruct(context.getBuildLog()));
    }

    @Test
    void build8() {
        Grammar grammar = GrammarExamples.get(G_8);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        LRAutomaton lr1Automaton = lr1AutomatonBuilder.build(grammar, context);
        LRAutomaton actual = builder.build(lr1Automaton, context);

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

        NonTerminal E_prime = new NonTerminal("E'");
        Rule r0 = new Rule(E_prime, E);

        var EOF = EndOfChainSymbol.getInstance();

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

        LRState iState = new LRState(
                new LR1Item(r3, 1, rParen),
                new LR1Item(r3, 1, plus),
                new LR1Item(r3, 1, EOF));

        LRState lParenState = new LRState(
                new LR1Item(r1, 0, rParen),
                new LR1Item(r1, 0, plus),
                new LR1Item(r2, 0, rParen),
                new LR1Item(r2, 0, plus),
                new LR1Item(r3, 0, rParen),
                new LR1Item(r3, 0, plus),
                new LR1Item(r4, 0, rParen),
                new LR1Item(r4, 0, plus),
                new LR1Item(r4, 1, rParen),
                new LR1Item(r4, 1, plus),
                new LR1Item(r4, 1, EOF));

        LRState E1State = new LRState(
                new LR1Item(r0, 1, EOF),
                new LR1Item(r2, 1, EOF),
                new LR1Item(r2, 1, plus));

        LRState T1T2State = new LRState(
                new LR1Item(r1, 1, rParen),
                new LR1Item(r1, 1, plus),
                new LR1Item(r1, 1, EOF));

        LRState E2E3State = new LRState(
                new LR1Item(r2, 1, rParen),
                new LR1Item(r2, 1, plus),
                new LR1Item(r4, 2, rParen),
                new LR1Item(r4, 2, plus),
                new LR1Item(r4, 2, EOF));

        LRState rParenState = new LRState(
                new LR1Item(r4, 3, rParen),
                new LR1Item(r4, 3, plus),
                new LR1Item(r4, 3, EOF));

        LRState plusState = new LRState(
                new LR1Item(r4, 0, rParen),
                new LR1Item(r3, 0, rParen),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r2, 2, plus),
                new LR1Item(r3, 0, plus),
                new LR1Item(r2, 2, EOF),
                new LR1Item(r2, 2, rParen),
                new LR1Item(r4, 0, plus),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r4, 0, EOF));

        LRState T3T4State = new LRState(
                new LR1Item(r2, 3, rParen),
                new LR1Item(r2, 3, plus),
                new LR1Item(r2, 3, EOF));

        String iStateName = "i1;i2";
        String lParenStateName = "(1;(2";
        String rParenStateName = ")1;)2";
        String plusStateName = "+1;+2";
        String T1T2StateName = "T1;T2";
        String T3T4StateName = "T3;T4";
        String E1StateName = "E1";
        String E2E3StateName = "E2;E3";

        Map<String, LRState> states = HashMap.newHashMap(9);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(iStateName, iState);
        states.put(lParenStateName, lParenState);
        states.put(rParenStateName, rParenState);
        states.put(plusStateName, plusState);
        states.put(T1T2StateName, T1T2State);
        states.put(T3T4StateName, T3T4State);
        states.put(E1StateName, E1State);
        states.put(E2E3StateName, E2E3State);

        Map<TransitionKey, String> transitions = HashMap.newHashMap(15);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, i), iStateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, lParen), lParenStateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, T), T1T2StateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, E), E1StateName);
        transitions.put(new TransitionKey(lParenStateName, i), iStateName);
        transitions.put(new TransitionKey(lParenStateName, lParen), lParenStateName);
        transitions.put(new TransitionKey(lParenStateName, T), T1T2StateName);
        transitions.put(new TransitionKey(lParenStateName, E), E2E3StateName);
        transitions.put(new TransitionKey(E2E3StateName, rParen), rParenStateName);
        transitions.put(new TransitionKey(E2E3StateName, plus), plusStateName);
        transitions.put(new TransitionKey(E1StateName, plus), plusStateName);
        transitions.put(new TransitionKey(plusStateName, i), iStateName);
        transitions.put(new TransitionKey(plusStateName, lParen), lParenStateName);
        transitions.put(new TransitionKey(plusStateName, T), T3T4StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
        assertEquals(expected, reconstructor.reconstruct(context.getBuildLog()));
    }

    @Test
    void build9() {
        Grammar grammar = GrammarExamples.get(G_9);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        LRAutomaton lr1Automaton = lr1AutomatonBuilder.build(grammar, context);
        LRAutomaton actual = builder.build(lr1Automaton, context);

        Terminal x = new Terminal("x");
        Terminal eq = new Terminal("=");
        Terminal star = new Terminal("*");

        NonTerminal S = new NonTerminal("S");
        NonTerminal L = new NonTerminal("L");
        NonTerminal R = new NonTerminal("R");

        Rule r1 = new Rule(S, L, eq, R);
        Rule r2 = new Rule(S, R);
        Rule r3 = new Rule(R, L);
        Rule r4 = new Rule(L, star, R);
        Rule r5 = new Rule(L, x);

        NonTerminal S_prime = new NonTerminal("S'");
        Rule r0 = new Rule(S_prime, S);

        var EOF = EndOfChainSymbol.getInstance();

        LRState startState = new LRState(
                new LR1Item(r0, 0, EOF),
                new LR1Item(r1, 0, EOF),
                new LR1Item(r2, 0, EOF),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r4, 0, eq),
                new LR1Item(r5, 0, EOF),
                new LR1Item(r5, 0, eq));

        LRState L1State = new LRState(
                new LR1Item(r1, 1, EOF),
                new LR1Item(r3, 1, EOF));

        LRState eqState = new LRState(
                new LR1Item(r1, 2, EOF),
                new LR1Item(r3, 0, EOF),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r5, 0, EOF));

        LRState R1State = new LRState(new LR1Item(r2, 1, EOF));

        LRState starState = new LRState(
                new LR1Item(r3, 0, EOF),
                new LR1Item(r3, 0, eq),
                new LR1Item(r4, 0, EOF),
                new LR1Item(r4, 0, eq),
                new LR1Item(r4, 1, EOF),
                new LR1Item(r4, 1, eq),
                new LR1Item(r5, 0, EOF),
                new LR1Item(r5, 0, eq));

        LRState xState = new LRState(
                new LR1Item(r5, 1, eq),
                new LR1Item(r5, 1, EOF));

        LRState L2L3State = new LRState(
                new LR1Item(r3, 1, eq),
                new LR1Item(r3, 1, EOF));

        LRState R2R4State = new LRState(
                new LR1Item(r4, 2, eq),
                new LR1Item(r4, 2, EOF));

        LRState R3State = new LRState(new LR1Item(r1, 3, EOF));

        LRState S1State = new LRState(new LR1Item(r0, 1, EOF));

        String L1StateName = "L1";
        String eqStateName = "=1";
        String R1StateName = "R1";
        String starStateName = "*1;*2";
        String xStateName = "x1;x2";
        String L2L3StateName = "L2;L3";
        String R2R4StateName = "R2;R4";
        String R3StateName = "R3";
        String S1StateName = "S1";

        Map<String, LRState> states = HashMap.newHashMap(10);
        states.put(INIT_AUTOMATON_STATE_NAME, startState);
        states.put(L1StateName, L1State);
        states.put(eqStateName, eqState);
        states.put(R1StateName, R1State);
        states.put(starStateName, starState);
        states.put(xStateName, xState);
        states.put(L2L3StateName, L2L3State);
        states.put(R2R4StateName, R2R4State);
        states.put(R3StateName, R3State);
        states.put(S1StateName, S1State);

        Map<TransitionKey, String> transitions = HashMap.newHashMap(15);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, x), xStateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, star), starStateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, L), L1StateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, R), R1StateName);
        transitions.put(new TransitionKey(INIT_AUTOMATON_STATE_NAME, S), S1StateName);
        transitions.put(new TransitionKey(L1StateName, eq), eqStateName);
        transitions.put(new TransitionKey(eqStateName, x), xStateName);
        transitions.put(new TransitionKey(eqStateName, star), starStateName);
        transitions.put(new TransitionKey(eqStateName, L), L2L3StateName);
        transitions.put(new TransitionKey(eqStateName, R), R3StateName);
        transitions.put(new TransitionKey(starStateName, x), xStateName);
        transitions.put(new TransitionKey(starStateName, star), starStateName);
        transitions.put(new TransitionKey(starStateName, L), L2L3StateName);
        transitions.put(new TransitionKey(starStateName, R), R2R4StateName);

        LRAutomaton expected = new LRAutomaton(states, transitions);
        assertEquals(expected, actual);
        assertEquals(expected, reconstructor.reconstruct(context.getBuildLog()));
    }
}
