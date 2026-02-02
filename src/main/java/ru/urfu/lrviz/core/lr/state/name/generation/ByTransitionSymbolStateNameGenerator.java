package ru.urfu.lrviz.core.lr.state.name.generation;

import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.HashMap;
import java.util.Map;

/**
 * Нумерует состояния с привязкой к грамматическому символу, по которому происходит переход. Переходов может быть
 * несколько. Поэтому имя формируется в формате
 */
public class ByTransitionSymbolStateNameGenerator implements StateNameGenerator {
    private final Map<GrammarSymbol, Integer> counters;

    @Override
    public String generate(LRState from, LRState to, GrammarSymbol through) {
        if (!counters.containsKey(through)) {
            counters.put(through, 2);
            return through.lexicalValue + "1";
        }
        int result = counters.get(through);
        counters.put(through, result + 1);
        return through.lexicalValue + result;
    }

    public ByTransitionSymbolStateNameGenerator() {
        this.counters = new HashMap<>();
    }
}
