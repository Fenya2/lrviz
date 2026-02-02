package ru.urfu.lrviz.core.lr;

import org.junit.jupiter.api.Test;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LR0ItemTest {

    @Test
    void createLR0ItemWithValidDotIndexExpectSuccess() {
        Rule rule = new Rule(
                new NonTerminal("S"),
                new Terminal("a"), new NonTerminal("A"));
        int dotIndex = 1;
        LR0Item item = new LR0Item(rule, dotIndex);
        assertEquals(rule, item.getRule());
        assertEquals(dotIndex, item.getDotIndex());
        assertFalse(item.isFinal());
    }

    @Test
    void createLR0ItemWithInvalidDotIndexExpectException() {
        Rule rule = new Rule(
                new NonTerminal("S"),
                new Terminal("a"), new Terminal("b"));
        int invalidDotIndex = 3;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LR0Item(rule, invalidDotIndex));
        assertTrue(exception.getMessage().contains("DotIndex 3 is not valid for rule"));
    }


    @Test
    void isFinalWhenDotAtEnd() {
        Rule rule = new Rule(
                new NonTerminal("S"),
                List.of(new Terminal("a"), new NonTerminal("B")));
        LR0Item finalItem = new LR0Item(rule, 2);
        assertTrue(finalItem.isFinal());
    }

    @Test
    void isNotFinalWhenDotInMiddle() {
        Rule rule = new Rule(
                new NonTerminal("A"),
                List.of(new Terminal("x"), new Terminal("y"), new Terminal("z")));
        LR0Item item = new LR0Item(rule, 1);
        assertFalse(item.isFinal());
    }

    @Test
    void createLR0ItemWithEmptyGetRuleDot() {
        Rule emptyRule = Rule.ofEmpty(new NonTerminal("S"));

        LR0Item item = new LR0Item(emptyRule, 0);

        assertNotNull(item);
        assertEquals(0, item.getDotIndex());
        assertTrue(item.isFinal());
    }
}