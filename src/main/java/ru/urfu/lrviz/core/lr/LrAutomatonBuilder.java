package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.grammar.Grammar;

/**
 * Билдер LR-автомата
 *
 * @author fenya
 * @since 23.02.2026
 */
public interface LrAutomatonBuilder {
    /**
     * @return тип автомата, который строит реализация
     */
    AutomatonType getBuildType();

    /**
     * Строит LR-автомат
     *
     * @param grammar грамматика
     * @param context подготовленный для построения контекст
     * @return построенный автомат
     */
    LRAutomaton build(Grammar grammar, BuildContext context);
}
