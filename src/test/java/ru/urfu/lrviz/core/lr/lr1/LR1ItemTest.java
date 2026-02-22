package ru.urfu.lrviz.core.lr.lr1;

import org.junit.jupiter.api.Test;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;
import ru.urfu.lrviz.core.grammar.Terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LR1ItemTest {
    @Test
    void shiftShouldIncrementDotIndexAndPreserveLookAheadSymbol() {
        Terminal a = new Terminal("a");
        Rule rule = new Rule(
                new NonTerminal("S"),
                a, new NonTerminal("A"));
        int initialDotIndex = 1;

        LR1Item originalItem = new LR1Item(rule, initialDotIndex, a);
        LR1Item shiftedItem = (LR1Item) originalItem.shift();

        assertEquals(rule, shiftedItem.getRule());
        assertEquals(initialDotIndex + 1, shiftedItem.getDotIndex());
        assertEquals(a, shiftedItem.getLookAheadSymbol());
    }
}