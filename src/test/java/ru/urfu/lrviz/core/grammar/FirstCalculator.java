package ru.urfu.lrviz.core.grammar;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.urfu.lrviz.core.grammar.GrammarServiceImpl.calculateForChain;

/**
 * Вычисляет множество FIRST от строки на основе переданного множества FIRST, построенного по какой-то грамматике
 *
 * @author fenya
 * @since 17.02.2026
 */
public class FirstCalculator {

    private final Map<GrammarSymbol, Set<FirstSetMember>> baseSet;

    public FirstCalculator(Map<GrammarSymbol, Set<FirstSetMember>> baseSet) {
        this.baseSet = baseSet;
    }

    /**
     * @return множество FIRST от переданной цепочки {@code chain}
     */
    public Set<FirstSetMember> calculateFor(List<GrammarSymbol> chain) {
        validateChain(chain);
        HashSet<FirstSetMember> result = new HashSet<>();
        calculateForChain(result, chain, baseSet);
        return result;
    }

    private void validateChain(List<GrammarSymbol> chain) {
        if (chain.isEmpty()) {
            throw new IllegalArgumentException("Chain can't be empty.");
        }
        if (!baseSet.keySet().containsAll(chain)) {
            throw new IllegalArgumentException("Chain contains symbol not included in base first set.");
        }
    }
}
