package ru.urfu.lrviz.core.grammar;

import java.util.*;

/**
 * Вычисляет множества FIRST от произвольных цепочек на основе переданного "Базисного множества"
 *
 * @author fenya
 * @since 14.02.2026
 */
public class FirstCalculator {
    private final Map<GrammarSymbol, Set<FirstSetMember>> base;

    public FirstCalculator(Map<GrammarSymbol, Set<FirstSetMember>> base) {
        this.base = base;
    }

    /**
     * @return множество FIRST от цепочки {@code chain}
     * @apiNote если {@code chain.isEmpty()}, считается, что FIRST вычисляется от {@link Epsilon#getInstance()}
     */
    public Set<FirstSetMember> calculateForChain(List<GrammarSymbol> chain) {
        if (chain.isEmpty()) {
            return Collections.singleton(Epsilon.getInstance());
        }
        Set<FirstSetMember> result = new HashSet<>();
        GrammarServiceImpl.calculateForChain(result, chain, base);
        return result;
    }
}
