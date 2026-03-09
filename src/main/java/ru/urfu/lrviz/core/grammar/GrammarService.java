package ru.urfu.lrviz.core.grammar;

import java.util.Map;
import java.util.Set;

public interface GrammarService {

    /**
     * Расширяет грамматику {@code grammar}
     */
    void expandGrammar(Grammar grammar);

    /**
     * Проверяет, что грамматика {@code grammar} является расширенной
     */
    boolean isGrammarExtended(Grammar grammar);

    /**
     * @return множества FIRST для нетерминалов и терминалов грамматики {@code grammar}
     */
    Map<GrammarSymbol, Set<FirstSetMember>> getFirst(Grammar grammar);
}
