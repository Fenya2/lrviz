package ru.urfu.lrviz.core.lr.state.name.generation;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.LRState;

/**
 * Генератор уникальных имен состояний lr-автомата. Один экзепляр используется
 * на построение одного автомата в одном процессе построения
 *
 * @author fenya
 * @since 02.02.2026
 */
public interface StateNameGenerator {

    /**
     * @param from    состояние, из которого осуществляется переход
     * @param to      состояние, в которое осуществляется переход
     * @param through грамматический символ, по которому осуществляется переход
     * @return уникальное имя состояния {@code from}
     */
    String generate(LRState from, LRState to, GrammarSymbol through);
}
