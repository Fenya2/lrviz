package ru.urfu.lrviz.core.grammar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GrammarServiceImplTest {

    @InjectMocks
    private GrammarServiceImpl grammarService;

    @Test
    void testExtendGrammarExpectNewStartSymbolAndRule() {
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
    void testExtendGrammarWhenStartSymbolAlreadyHasApostropheExpectMultipleApostrophes() {
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
}