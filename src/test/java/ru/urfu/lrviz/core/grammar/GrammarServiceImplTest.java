package ru.urfu.lrviz.core.grammar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.lrviz.core.GrammarExamples;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("java:S117") // имена переменных здесь оправданы
@ExtendWith(MockitoExtension.class)
class GrammarServiceImplTest {

    @InjectMocks
    private GrammarServiceImpl grammarService;

    @Test
    void extendGrammarExpectNewStartSymbolAndRule() {
        Terminal singleTerminal = new Terminal("a");
        NonTerminal startSymbol = new NonTerminal("S");
        Set<Terminal> terminals = Collections.singleton(singleTerminal);
        Set<NonTerminal> nonTerminals = Collections.singleton(startSymbol);
        Set<Rule> rules = Collections.singleton(new Rule(startSymbol, singleTerminal));
        Grammar grammar = new Grammar(terminals, nonTerminals, rules, startSymbol);

        grammarService.extendGrammar(grammar);

        NonTerminal newStartSymbol = new NonTerminal("S'");
        assertEquals(newStartSymbol, grammar.getStartSymbol());
        assertTrue(grammar.getRules().contains(new Rule(newStartSymbol, Collections.singletonList(startSymbol))));
    }

    @Test
    void extendGrammarWhenStartSymbolAlreadyHasApostropheExpectMultipleApostrophes() {
        Terminal singleTerminal = new Terminal("a");
        NonTerminal originalStartSymbol = new NonTerminal("S");
        NonTerminal startSymbol = new NonTerminal("S'");
        Set<Terminal> terminals = Collections.singleton(singleTerminal);
        Set<NonTerminal> nonTerminals = Set.of(originalStartSymbol, startSymbol);
        Set<Rule> rules = Set.of(
                new Rule(startSymbol, originalStartSymbol),
                new Rule(originalStartSymbol, singleTerminal));
        Grammar grammar = new Grammar(terminals, nonTerminals, rules, originalStartSymbol);

        grammarService.extendGrammar(grammar);

        NonTerminal newStartSymbol = new NonTerminal("S''");
        assertEquals(newStartSymbol, grammar.getStartSymbol());
        assertTrue(grammar.getRules().contains(new Rule(newStartSymbol, Collections.singletonList(originalStartSymbol))));
    }

    @Test
    void getFirst1() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_1));

        Terminal REAL = new Terminal("r");
        Terminal INT = new Terminal("i");
        Terminal a = new Terminal("a");
        Terminal SEMICOLON = new Terminal(";");
        Set<FirstSetMember> aFirst = Collections.singleton(a);

        Map<GrammarSymbol, Set<FirstSetMember>> expected = Map.of(
                new NonTerminal("D"), Set.of(REAL, INT),
                new NonTerminal("T"), Set.of(REAL, INT),
                new NonTerminal("L"), aFirst,
                REAL, Collections.singleton(REAL),
                INT, Collections.singleton(INT),
                a, aFirst,
                SEMICOLON, Collections.singleton(SEMICOLON));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFirst2() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_2));

        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Set<FirstSetMember> abFirst = Set.of(a, b);
        Set<FirstSetMember> aFirst = Collections.singleton(a);
        Set<FirstSetMember> bFirst = Collections.singleton(b);

        Map<GrammarSymbol, Set<FirstSetMember>> expected = Map.of(
                new NonTerminal("S"), abFirst,
                new NonTerminal("A"), abFirst,
                a, aFirst,
                b, bFirst);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFirst3() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_3));

        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        FirstSetMember EPSILON = Epsilon.getInstance();

        Map<GrammarSymbol, Set<FirstSetMember>> expected = Map.of(
                new NonTerminal("S"), Set.of(EPSILON, LPAREN),
                new NonTerminal("A"), Set.of(EPSILON, b),
                new NonTerminal("B"), Set.of(b),
                new NonTerminal("C"), Set.of(EPSILON, c),
                LPAREN, Collections.singleton(LPAREN),
                RPAREN, Collections.singleton(RPAREN),
                b, Collections.singleton(b),
                c, Collections.singleton(c));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFirst4() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_4));

        Terminal u = new Terminal("u");
        Terminal v = new Terminal("v");
        Terminal w = new Terminal("w");
        Terminal x = new Terminal("x");
        Terminal y = new Terminal("y");
        Terminal z = new Terminal("z");
        FirstSetMember EPSILON = Epsilon.getInstance();


        Map<GrammarSymbol, Set<FirstSetMember>> expected = new HashMap<>();
        expected.put(new NonTerminal("S"), Set.of(u));
        expected.put(new NonTerminal("D"), Set.of(EPSILON, y, x));
        expected.put(new NonTerminal("B"), Set.of(w));
        expected.put(new NonTerminal("E"), Set.of(EPSILON, y));
        expected.put(new NonTerminal("F"), Set.of(EPSILON, x));
        expected.put(u, Collections.singleton(u));
        expected.put(v, Collections.singleton(v));
        expected.put(w, Collections.singleton(w));
        expected.put(x, Collections.singleton(x));
        expected.put(y, Collections.singleton(y));
        expected.put(z, Collections.singleton(z));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFirst5() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_5));

        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");
        FirstSetMember EPSILON = Epsilon.getInstance();

        Map<GrammarSymbol, Set<FirstSetMember>> expected = Map.of(
                new NonTerminal("S"), Set.of(EPSILON, LPAREN),
                LPAREN, Collections.singleton(LPAREN),
                RPAREN, Collections.singleton(RPAREN));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFirst6() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_6));

        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Terminal d = new Terminal("d");

        Map<GrammarSymbol, Set<FirstSetMember>> expected = Map.of(
                new NonTerminal("S"), Set.of(d, b),
                new NonTerminal("A"), Set.of(d),
                a, Collections.singleton(a),
                b, Collections.singleton(b),
                c, Collections.singleton(c),
                d, Collections.singleton(d));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFirst7() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_7));

        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");

        Map<GrammarSymbol, Set<FirstSetMember>> expected = Map.of(
                new NonTerminal("S"), Set.of(a),
                new NonTerminal("A"), Set.of(a),
                new NonTerminal("B"), Set.of(b),
                new NonTerminal("C"), Set.of(c),
                a, Collections.singleton(a),
                b, Collections.singleton(b),
                c, Collections.singleton(c));
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFirst8() {
        Map<GrammarSymbol, Set<FirstSetMember>> actual = grammarService.getFirst(GrammarExamples.get(GrammarExamples.G_8));

        Terminal i = new Terminal("i");
        Terminal plus = new Terminal("+");
        Terminal LPAREN = new Terminal("(");
        Terminal RPAREN = new Terminal(")");

        Map<GrammarSymbol, Set<FirstSetMember>> expected = Map.of(
                new NonTerminal("E"), Set.of(i, LPAREN),
                new NonTerminal("T"), Set.of(i, LPAREN),
                i, Collections.singleton(i),
                plus, Collections.singleton(plus),
                LPAREN, Collections.singleton(LPAREN),
                RPAREN, Collections.singleton(RPAREN));
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}