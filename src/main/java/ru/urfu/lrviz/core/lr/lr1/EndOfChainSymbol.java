package ru.urfu.lrviz.core.lr.lr1;

/**
 * @author fenya
 * @since 14.02.2026
 */
public class EndOfChainSymbol implements LookAheadSymbol {
    public static final String END_OF_CHAIN_SYMBOL = "⊣";

    private static final EndOfChainSymbol instance = new EndOfChainSymbol();

    public static EndOfChainSymbol getInstance() {
        return instance;
    }

    private EndOfChainSymbol() {
    }

    @Override
    public String asString() {
        return END_OF_CHAIN_SYMBOL;
    }

    @Override
    public String toString() {
        return asString();
    }
}
