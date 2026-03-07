package ru.urfu.lrviz.core.lr.state.name.generation;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.LRState;

/**
 * Дает каждому новому состоянию новый номер. Нумерация начинается с 0
 *
 * @author fenya
 * @since 22.02.2026
 */
public class EndToEndNumerationStateNameGenerator implements StateNameGenerator {
    private int counter = 1;

    @Override
    public String getInitAutomatonStateName() {
        return "0";
    }

    @Override
    public String generate(LRState from, LRState to, GrammarSymbol through) {
        return Integer.toString(counter++);
    }
}
