package ru.urfu.lrviz.core.automaton;

/**
 * Элемент области определения функции переходов конечных автоматов
 */
public record DFATransitionMapKey<State, Symbol>(State state, Symbol symbol) {
}