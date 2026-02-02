package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;

public interface LRAutomatonsBuilder<S extends LRAutomatonState> {

    AutomatonType getBuildType();

    DFA<S, GrammarSymbol> build(Grammar grammar, BuildContext context);
}
