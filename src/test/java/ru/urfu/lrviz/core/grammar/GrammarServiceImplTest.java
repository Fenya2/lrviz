package ru.urfu.lrviz.core.grammar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.urfu.lrviz.core.GrammarExamples;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Terminal REAL = new Terminal("real");
        Terminal INT = new Terminal("int");
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
}