package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;

public record LR0AutomataBuildResult(BuildLog buildLog, DFA<LR0AutomatonState, GrammarSymbol> lr0Automata) {
}
