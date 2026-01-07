package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.grammar.Grammar;

public interface LRAutomatonsBuilder {
    LR0AutomataBuildResult buildLR0Automata(Grammar grammar);

}
