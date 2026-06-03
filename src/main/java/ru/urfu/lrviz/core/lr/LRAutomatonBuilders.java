package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.grammar.Grammar;

/**
 * Фасад над билдерами LR-автоматов
 *
 * @author fenya
 * @since 23.02.2026
 */
public interface LRAutomatonBuilders {
    /**
     * Строит LR-автомат
     *
     * @param grammar грамматика, автомат которой нужно построить
     * @param type    тип автомата, который требуется построить
     * @param context контекст для построения автомата типа {@code type}
     * @return построенный автомат
     */
    LRAutomaton build(Grammar grammar, AutomatonType type, BuildContext context);
}
