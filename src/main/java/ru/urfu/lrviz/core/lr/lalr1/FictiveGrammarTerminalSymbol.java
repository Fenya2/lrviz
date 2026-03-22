package ru.urfu.lrviz.core.lr.lalr1;

import ru.urfu.lrviz.core.grammar.FirstSetMember;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

/**
 * Терминальный символ, которого гарантированно нет в любой грамматике
 *
 * @author fenya
 * @since 21.03.2026
 */
public class FictiveGrammarTerminalSymbol extends Terminal implements LookAheadSymbol, FirstSetMember {
    private static final String STRING_REPRESENTATION = "#";
    private static final FictiveGrammarTerminalSymbol instance = new FictiveGrammarTerminalSymbol();

    public static FictiveGrammarTerminalSymbol getInstance() {
        return instance;
    }

    private FictiveGrammarTerminalSymbol() {
        super(STRING_REPRESENTATION);
    }

    @Override
    public String asString() {
        return STRING_REPRESENTATION;
    }

    @Override
    public String toString() {
        return asString();
    }
}
