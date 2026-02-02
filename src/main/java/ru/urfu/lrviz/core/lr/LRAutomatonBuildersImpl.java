package ru.urfu.lrviz.core.lr;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.lrnew.BuildContext;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class LRAutomatonBuildersImpl implements LRAutomatonBuilders {

    private final Map<AutomatonType, LRAutomatonsBuilder<?>> builders;

    public LRAutomatonBuildersImpl(List<LRAutomatonsBuilder<?>> builders) {
        this.builders = new EnumMap<>(AutomatonType.class);
        for (LRAutomatonsBuilder<?> builder : builders) {
            this.builders.put(builder.getBuildType(), builder);
        }
    }

    @Override
    public DFA<? extends LRAutomatonState, GrammarSymbol> build(Grammar grammar, AutomatonType type, BuildContext context) {
        return builders.get(type).build(grammar, context);
    }
}
