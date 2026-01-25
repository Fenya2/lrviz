package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;

public interface LRAutomatonBuilders {
    DFA<? extends LRAutomatonState, GrammarSymbol> build(Grammar grammar, AutomatonType type, BuildContext context);
}
