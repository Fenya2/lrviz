package ru.urfu.lrviz.core.lr;

/**
 * Стратегия генерации имен состояний LR-автомата при его построении
 *
 * @author fenya
 * @since 07.03.2026
 */
public enum StateNamesGenerationStrategy {
    END_TO_END_NUMERIC,
    BY_TRANSITION_SYMBOL
}
