package ru.urfu.lrviz.core.grammar;

import java.util.Map;
import java.util.Set;

public interface GrammarService {
    void extendGrammar(Grammar grammar);

    /**
     * @return множества FIRS для нетерминалов и терминалов переданной грамматики
     */
    Map<GrammarSymbol, Set<FirstSetMember>> getFirst(Grammar grammar);
}
