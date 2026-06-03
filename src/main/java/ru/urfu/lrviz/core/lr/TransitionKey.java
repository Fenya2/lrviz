package ru.urfu.lrviz.core.lr;

/**
 *
 * @author fenya
 * @since 20.04.2026
 */
public record TransitionKey(String stateName, TransitionSymbol symbol) {
    @Override
    public String toString() {
        return "(" + stateName + ", " + symbol.asString() + ")";
    }
}
