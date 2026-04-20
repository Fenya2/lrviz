package ru.urfu.lrviz.core.lr;

/**
 * Переход в LR-автомате
 *
 * @author fenya
 * @since 20.04.2026
 */
public record TransitionEntry(TransitionKey key, String toStateName) {
}
