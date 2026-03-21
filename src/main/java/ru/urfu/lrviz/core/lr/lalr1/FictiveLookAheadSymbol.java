package ru.urfu.lrviz.core.lr.lalr1;

import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

/**
 * @author fenya
 * @since 21.03.2026
 */
public class FictiveLookAheadSymbol implements LookAheadSymbol {
    public static final String FICTIVE_LOOKAHEAD_SYMBOL = "#";

    private static final FictiveLookAheadSymbol instance = new FictiveLookAheadSymbol();

    public static FictiveLookAheadSymbol getInstance() {
        return instance;
    }

    private FictiveLookAheadSymbol() {
    }

    @Override
    public String asString() {
        return FICTIVE_LOOKAHEAD_SYMBOL;
    }

    @Override
    public String toString() {
        return asString();
    }
}
