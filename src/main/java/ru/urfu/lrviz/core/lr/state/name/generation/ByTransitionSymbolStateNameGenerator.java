package ru.urfu.lrviz.core.lr.state.name.generation;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.HashMap;
import java.util.Map;

/**
 * Нумерует состояния с привязкой к грамматическому символу, по которому происходит переход. Переходов может быть
 * несколько. Поэтому имя формируется в формате {@link GrammarSymbol#lexicalValue} + {@code НОМЕР_СОСТОЯНИЯ}. Номер
 * начинаяется с 1, увеличивается с шагом 1
 */
public class ByTransitionSymbolStateNameGenerator implements StateNameGenerator {
    private static final String INIT_AUTOMATON_STATE_NAME = "∇";

    private final Map<GrammarSymbol, Integer> counters;

    @Override
    public String getInitAutomatonStateName() {
        return INIT_AUTOMATON_STATE_NAME;
    }

    @Override
    public String generate(LRState from, LRState to, GrammarSymbol through) {
        int result = counters.getOrDefault(through, 1);
        counters.put(through, result + 1);
        return through.lexicalValue + result;
    }

    public ByTransitionSymbolStateNameGenerator() {
        this.counters = new HashMap<>();
    }
}
