package ru.urfu.lrviz.core.lr;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.automaton.DFATransitionMapKey;
import ru.urfu.lrviz.core.grammar.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class LR0AutomatonsBuilderTest {

    @Autowired
    private LR0AutomatonsBuilder service;

    /**
     * D => T L;
     * T => int | real;
     * L => L : a | a;
     */
    @Test
    void build() {
        Terminal INT = new Terminal("int");
        Terminal REAL = new Terminal("real");
        Terminal SEMICOLON = new Terminal(";");
        Terminal a = new Terminal("a");

        Set<Terminal> terminals = Set.of(INT, REAL, SEMICOLON, a);

        NonTerminal D = new NonTerminal("D");
        NonTerminal T = new NonTerminal("T");
        NonTerminal L = new NonTerminal("L");

        Set<NonTerminal> nonTerminals = Set.of(D, T, L);

        NonTerminal D_prime = new NonTerminal("D'");
        Rule r0 = new Rule(D_prime, D);
        Rule r1 = new Rule(D, T, L);
        Rule r2 = new Rule(T, REAL);
        Rule r3 = new Rule(T, INT);
        Rule r4 = new Rule(L, L, SEMICOLON, a);
        Rule r5 = new Rule(L, a);

        Set<Rule> rules = Set.of(r1, r2, r3, r4, r5);

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, D);

        BuildContext buildContext = new BuildContext(new BuildLog());
        DFA<LR0AutomatonState, GrammarSymbol> actual = (DFA<LR0AutomatonState, GrammarSymbol>) service.build(grammar, buildContext);

        LR0Item i0 = new LR0Item(r0, 0);
        LR0Item i1 = new LR0Item(r1, 0);
        LR0Item i2 = new LR0Item(r2, 0);
        LR0Item i3 = new LR0Item(r3, 0);

        LR0Item i4 = new LR0Item(r0, 1);

        LR0Item i5 = new LR0Item(r1, 1);
        LR0Item i6 = new LR0Item(r4, 0);
        LR0Item i7 = new LR0Item(r5, 0);

        LR0Item i8 = new LR0Item(r2, 1);
        LR0Item i9 = new LR0Item(r3, 1);
        LR0Item i10 = new LR0Item(r5, 1);

        LR0Item i11 = new LR0Item(r4, 1);
        LR0Item i12 = new LR0Item(r1, 2);

        LR0Item i13 = new LR0Item(r4, 2);
        LR0Item i14 = new LR0Item(r4, 3);


        LR0AutomatonState s0 = new LR0AutomatonState("∇", Set.of(i0, i1, i2, i3));
        LR0AutomatonState s1 = new LR0AutomatonState("D1", Set.of(i4));
        LR0AutomatonState s2 = new LR0AutomatonState("T1", Set.of(i5, i6, i7));
        LR0AutomatonState s3 = new LR0AutomatonState("real1", Set.of(i8));
        LR0AutomatonState s4 = new LR0AutomatonState("int1", Set.of(i9));
        LR0AutomatonState s5 = new LR0AutomatonState("a1", Set.of(i10));
        LR0AutomatonState s6 = new LR0AutomatonState("L1", Set.of(i11, i12));
        LR0AutomatonState s7 = new LR0AutomatonState(";1", Set.of(i13));
        LR0AutomatonState s8 = new LR0AutomatonState("a2", Set.of(i14));

        Set<LR0AutomatonState> states = Set.of(s0, s1, s2, s3, s4, s5, s6, s7, s8);

        Set<GrammarSymbol> alphabet = Set.of(a, SEMICOLON, REAL, INT, D, T, L, D_prime);

        Map<DFATransitionMapKey<LR0AutomatonState, GrammarSymbol>, LR0AutomatonState> transitions = new HashMap<>();

        transitions.put(new DFATransitionMapKey<>(s0, D), s1);
        transitions.put(new DFATransitionMapKey<>(s0, T), s2);
        transitions.put(new DFATransitionMapKey<>(s0, REAL), s3);
        transitions.put(new DFATransitionMapKey<>(s0, INT), s4);

        transitions.put(new DFATransitionMapKey<>(s2, L), s6);
        transitions.put(new DFATransitionMapKey<>(s2, a), s5);

        transitions.put(new DFATransitionMapKey<>(s6, SEMICOLON), s7);
        transitions.put(new DFATransitionMapKey<>(s7, a), s8);

        DFA<LR0AutomatonState, GrammarSymbol> expected = new DFA<>(states, alphabet, transitions, s0, Set.of());

        Assertions.assertEquals(expected, actual);
    }
}