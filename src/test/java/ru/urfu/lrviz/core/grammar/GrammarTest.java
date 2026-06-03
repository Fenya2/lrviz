package ru.urfu.lrviz.core.grammar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GrammarTest {
    @Test
    void createGrammarWithValidParametersExpectSuccess() {
        Set<Terminal> terminals = Set.of(new Terminal("a"), new Terminal("b"));
        Set<NonTerminal> nonTerminals = Set.of(new NonTerminal("S"), new NonTerminal("A"));

        Rule rule1 = new Rule(new NonTerminal("S"), List.of(new Terminal("a"), new NonTerminal("A")));
        Rule rule2 = new Rule(new NonTerminal("A"), List.of(new Terminal("b")));
        Set<Rule> rules = Set.of(rule1, rule2);

        NonTerminal startSymbol = new NonTerminal("S");

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, startSymbol);

        assertNotNull(grammar);
        assertEquals(terminals, grammar.getTerminals());
        assertEquals(nonTerminals, grammar.getNonTerminals());
        assertEquals(rules, grammar.getRules());
        assertEquals(startSymbol, grammar.getStartSymbol());
    }

    @Test
    void createGrammarWithInvalidLeftPartInRuleExpectException() {
        Set<Terminal> terminals = Set.of(new Terminal("a"));
        Set<NonTerminal> nonTerminals = Set.of(new NonTerminal("S"));

        // Правило с левой частью "A", которой нет в nonTerminals
        Rule rule1 = new Rule(new NonTerminal("S"), List.of(new Terminal("a")));
        Rule invalidRule = new Rule(new NonTerminal("A"), List.of(new Terminal("a")));
        Set<Rule> rules = Set.of(rule1, invalidRule);

        NonTerminal startSymbol = new NonTerminal("S");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Grammar(terminals, nonTerminals, rules, startSymbol));
        assertTrue(exception.getMessage().contains("Left parts of rules contains nonterminal not included in nonterminals set"));
    }

    @Test
    void createGrammarWithInvalidSymbolInRightPartExpectException() {
        Set<Terminal> terminals = Set.of(new Terminal("a"));
        Set<NonTerminal> nonTerminals = Set.of(new NonTerminal("S"));

        // Правило с правой частью, содержащей терминал "b", которого нет в terminals
        Rule invalidRule = new Rule(new NonTerminal("S"),
                List.of(new Terminal("a"), new Terminal("b")));
        Set<Rule> rules = Set.of(invalidRule);

        NonTerminal startSymbol = new NonTerminal("S");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Grammar(terminals, nonTerminals, rules, startSymbol));
        assertTrue(exception.getMessage().contains("Right parts of rules contains symbol not included in any grammar alphabet"));
    }

    @Test
    void createGrammarWithInvalidStartSymbolExpectException() {
        Set<Terminal> terminals = Set.of(new Terminal("a"));
        Set<NonTerminal> nonTerminals = Set.of(new NonTerminal("S"));

        Rule rule = new Rule(new NonTerminal("S"), List.of(new Terminal("a")));
        Set<Rule> rules = Set.of(rule);

        // Стартовый символ "A" отсутствует в nonTerminals
        NonTerminal invalidStartSymbol = new NonTerminal("A");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Grammar(terminals, nonTerminals, rules, invalidStartSymbol)
        );
        assertTrue(exception.getMessage().contains("Nonterminals don't contains start symbol"));
    }

    @Test
    void addNonTerminalExpectAddedToSet() {
        Grammar grammar = createSimpleGrammar();
        NonTerminal newNonTerminal = new NonTerminal("B");

        grammar.addNonTerminal(newNonTerminal);

        assertTrue(grammar.getNonTerminals().contains(newNonTerminal));
    }

    @Test
    void addValidRuleExpectAddedToSet() {
        Grammar grammar = createSimpleGrammar();
        Rule newRule = new Rule(new NonTerminal("S"), List.of(new NonTerminal("A"), new NonTerminal("A")));

        grammar.addRule(newRule);

        assertTrue(grammar.getRules().contains(newRule));
    }

    @Test
    void addRuleWithInvalidLeftPartExpectException() {
        Grammar grammar = createSimpleGrammar();
        // Попытка добавить правило с левой частью "B", которой нет в нетерминалах
        Rule invalidRule = new Rule(new NonTerminal("B"), List.of(new Terminal("a")));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> grammar.addRule(invalidRule)
        );
        assertTrue(exception.getMessage().contains("Nonterminals don't contains rule left part"));
    }

    @Test
    void addRuleWithInvalidSymbolInRightPartExpectException() {
        Grammar grammar = createSimpleGrammar();
        // Попытка добавить правило с терминалом "c", которого нет в алфавите
        Rule invalidRule = new Rule(new NonTerminal("S"),
                List.of(new Terminal("a"), new Terminal("c")));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> grammar.addRule(invalidRule)
        );
        assertTrue(exception.getMessage().contains("Nonterminals don't contains rule right part"));
    }

    @Test
    void setValidStartSymbolExpectSuccess() {
        Grammar grammar = createSimpleGrammar();
        NonTerminal newStartSymbol = new NonTerminal("A");

        grammar.setStartSymbol(newStartSymbol);

        assertEquals(newStartSymbol, grammar.getStartSymbol());
    }

    @Test
    void setInvalidStartSymbolExpectException() {
        Grammar grammar = createSimpleGrammar();
        NonTerminal invalidStartSymbol = new NonTerminal("B"); // Не существует в грамматике

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> grammar.setStartSymbol(invalidStartSymbol)
        );
        assertTrue(exception.getMessage().contains("Current nonterminals set don't contains new start symbol"));
    }

    @Test
    void getNonTerminalsExpectReturnCopy() {
        Grammar grammar = createSimpleGrammar();
        Set<NonTerminal> nonTerminals = grammar.getNonTerminals();

        assertNotNull(nonTerminals);
        assertEquals(2, nonTerminals.size());
        assertTrue(nonTerminals.contains(new NonTerminal("S")));
        assertTrue(nonTerminals.contains(new NonTerminal("A")));
    }

    @Test
    void getStartSymbolExpectCorrectSymbol() {
        Grammar grammar = createSimpleGrammar();

        assertEquals(new NonTerminal("S"), grammar.getStartSymbol());
    }

    private Grammar createSimpleGrammar() {
        Set<Terminal> terminals = Set.of(new Terminal("a"));
        Set<NonTerminal> nonTerminals = Set.of(new NonTerminal("S"), new NonTerminal("A"));

        Rule rule = new Rule(new NonTerminal("S"), new Terminal("a"), new NonTerminal("A"));
        Set<Rule> rules = Collections.singleton(rule);

        NonTerminal startSymbol = new NonTerminal("S");

        return new Grammar(terminals, nonTerminals, rules, startSymbol);
    }

    @Test
    void getAlternativesFor() {
        Set<Terminal> terminals = Set.of(new Terminal("a"));
        NonTerminal startSymbol = new NonTerminal("S");
        NonTerminal anotherSymbol = new NonTerminal("A");
        Set<NonTerminal> nonTerminals = new HashSet<>(Set.of(startSymbol, anotherSymbol));
        Rule alternative1 = new Rule(startSymbol, new Terminal("a"));
        Rule alternative2 = new Rule(startSymbol, new Terminal("a"), new Terminal("a"));
        Set<Rule> rules = Set.of(
                alternative1,
                alternative2,
                new Rule(anotherSymbol, new Terminal("a")));

        Grammar grammar = new Grammar(terminals, nonTerminals, rules, startSymbol);
        Set<Rule> alternatives = grammar.getAlternativesFor(startSymbol);
        Assertions.assertEquals(Set.of(alternative1, alternative2), alternatives);
    }
}