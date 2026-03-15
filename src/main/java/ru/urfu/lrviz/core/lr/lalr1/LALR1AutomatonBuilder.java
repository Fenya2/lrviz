package ru.urfu.lrviz.core.lr.lalr1;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.AutomatonType;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LrAutomatonBuilder;
import ru.urfu.lrviz.core.lr.lr1.LR1AutomatonBuilder;
import ru.urfu.lrviz.core.lr.operations.BuildLRAutomatonOperation;

import static ru.urfu.lrviz.core.lr.AutomatonType.LALR;

/**
 * @author fenya
 * @since 07.03.2026
 */
@Component
public class LALR1AutomatonBuilder implements LrAutomatonBuilder {
    private final LR1AutomatonBuilder lr1AutomatonBuilder;
    private final ClassicLALR1AutomatonBuilder classicLALR1AutomatonBuilder;

    public LALR1AutomatonBuilder(LR1AutomatonBuilder lr1AutomatonBuilder, ClassicLALR1AutomatonBuilder classicLALR1AutomatonBuilder) {
        this.lr1AutomatonBuilder = lr1AutomatonBuilder;
        this.classicLALR1AutomatonBuilder = classicLALR1AutomatonBuilder;
    }

    @Override
    public AutomatonType getBuildType() {
        return LALR;
    }

    @Override
    public LRAutomaton build(Grammar grammar, BuildContext context) {
        context.getBuildLog().append(new BuildLRAutomatonOperation(getBuildType()));
        return switch (context.getLalr1BuildAlgorithm()) {
            case CLASSIC -> buildWithClassicAlgorithm(grammar, context);
            case CHANNEL -> buildWithChannelAlgorithm(grammar, context);
            case null -> throw new IllegalArgumentException("LALR build algorithm is not specified.");
        };
    }

    private LRAutomaton buildWithClassicAlgorithm(Grammar grammar, BuildContext context) {
        return classicLALR1AutomatonBuilder.build(lr1AutomatonBuilder.build(grammar, context), context);
    }

    private LRAutomaton buildWithChannelAlgorithm(Grammar grammar, BuildContext context) {
        throw new UnsupportedOperationException();
    }
}
