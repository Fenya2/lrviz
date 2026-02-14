package ru.urfu.lrviz.core.lr.lr1;

/**
 *
 * @author fenya
 * @since 14.02.2026
 */
public class EndOfFileSymbol implements LookAheadSymbol {
    private static final EndOfFileSymbol instance = new EndOfFileSymbol();

    public static EndOfFileSymbol getInstance() {
        return instance;
    }

    private EndOfFileSymbol() {
    }
}
