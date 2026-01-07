package ru.urfu.lrviz.core.grammar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Правило грамматики. Необходимо описание только контекстно-свободных грамматик, поэтому левая часть правила состоит
 * только из одного нетерминального символа
 */
public record Rule(NonTerminal left, List<GrammarSymbol> right) {

    public static Rule ofEmpty(NonTerminal left) {
        return new Rule(left, Collections.emptyList());
    }

    public Rule(NonTerminal nonTerminal, GrammarSymbol... right) {
        this(nonTerminal, Arrays.asList(right));
    }

    @Override
    public String toString() {
        return left + "→" + String.join("", right.stream().map(grammarSymbol -> grammarSymbol.lexicalValue).toList());
    }

    public boolean isEmpty() {
        return right.isEmpty();
    }
}