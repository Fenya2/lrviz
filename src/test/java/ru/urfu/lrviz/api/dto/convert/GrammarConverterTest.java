package ru.urfu.lrviz.api.dto.convert;

import org.junit.jupiter.api.Test;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GrammarConverterTest {

    private final GrammarDtoConverter mapper = new GrammarDtoConverter();

    @Test
    void convert_basicGrammar_success() {
        GrammarDto dto = new GrammarDto(
                List.of("a", "b"),
                List.of("S"),
                List.of(
                        new RuleDto("S", "aS"),
                        new RuleDto("S", "b")),
                "S"
        );

        Grammar grammar = mapper.convert(dto);

        // terminals
        assertEquals(
                Set.of(new Terminal("a"), new Terminal("b")),
                grammar.getTerminals());

        // non-terminals
        assertEquals(
                Set.of(new NonTerminal("S")),
                grammar.getNonTerminals());

        // start symbol
        assertEquals(new NonTerminal("S"), grammar.getStartSymbol());

        // rules
        Set<Rule> rules = grammar.getRules();
        assertEquals(2, rules.size());

        Rule rule1 = new Rule(
                new NonTerminal("S"),
                List.of(new Terminal("a"), new NonTerminal("S")));
        Rule rule2 = new Rule(
                new NonTerminal("S"),
                List.of(new Terminal("b")));

        assertTrue(rules.contains(rule1));
        assertTrue(rules.contains(rule2));
    }

    @Test
    void convert_singleTerminalRule_success() {
        GrammarDto dto = new GrammarDto(
                List.of("a"),
                List.of("S"),
                List.of(new RuleDto("S", "a")),
                "S");

        Grammar grammar = mapper.convert(dto);

        assertEquals(1, grammar.getRules().size());

        Rule rule = grammar.getRules().iterator().next();
        assertEquals(new NonTerminal("S"), rule.left());
        assertEquals(List.of(new Terminal("a")), rule.right());
    }

    @Test
    void convert_unknownSymbolInRule_throwsException() {
        GrammarDto dto = new GrammarDto(
                List.of("a"),
                List.of("S"),
                List.of(new RuleDto("S", "aX")),
                "S");

        assertThrows(IllegalArgumentException.class, () ->
                mapper.convert(dto));
    }

    @Test
    void convert_startSymbolNotInNonTerminals_throwsException() {
        GrammarDto dto = new GrammarDto(
                List.of("a"),
                List.of("S"),
                List.of(new RuleDto("S", "a")),
                "A");

        assertThrows(IllegalArgumentException.class, () ->
                mapper.convert(dto));
    }

    @Test
    void convert_emptyRightPart_epsilonRule_success() {
        GrammarDto dto = new GrammarDto(
                Collections.emptyList(),
                List.of("S"),
                List.of(
                        new RuleDto("S", "")),
                "S");

        Grammar grammar = mapper.convert(dto);

        assertEquals(1, grammar.getRules().size());

        Rule rule = grammar.getRules().iterator().next();

        assertEquals(new NonTerminal("S"), rule.left());
        assertTrue(rule.right().isEmpty(), "Right part of epsilon rule must be empty");
    }
}
