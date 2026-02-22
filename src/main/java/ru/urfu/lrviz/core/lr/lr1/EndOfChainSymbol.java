package ru.urfu.lrviz.core.lr.lr1;

/**
 *
 * @author fenya
 * @since 14.02.2026
 */
public class EndOfChainSymbol implements LookAheadSymbol {
    private static final EndOfChainSymbol instance = new EndOfChainSymbol();

    public static EndOfChainSymbol getInstance() {
        return instance;
    }

    private EndOfChainSymbol() {
    }

    @Override
    public String asString() {
        return "⊣";
    }

    @Override
    public String toString() {
        return asString();
    }
}
