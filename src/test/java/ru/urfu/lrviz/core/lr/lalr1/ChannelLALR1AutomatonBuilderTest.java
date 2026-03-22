package ru.urfu.lrviz.core.lr.lalr1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.BuildContextCreator;
import ru.urfu.lrviz.core.lr.BuildOptions;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.lr0.LR0AutomatonBuilder;

import static ru.urfu.lrviz.core.GrammarExamples.G_9;
import static ru.urfu.lrviz.core.lr.AutomatonType.LALR;

@SuppressWarnings("java:S117") // имена переменных здесь оправданы
@SpringBootTest
class ChannelLALR1AutomatonBuilderTest {
    private final LR0AutomatonBuilder lr0AutomatonBuilder;
    private final BuildContextCreator contextCreator;
    private final ChannelLALR1AutomatonBuilder builder;

    @Autowired
    ChannelLALR1AutomatonBuilderTest(LR0AutomatonBuilder lr1AutomatonBuilder, BuildContextCreator contextCreator, ChannelLALR1AutomatonBuilder builder) {
        this.lr0AutomatonBuilder = lr1AutomatonBuilder;
        this.contextCreator = contextCreator;
        this.builder = builder;
    }

    @Test
    void build9() {
        Grammar grammar = GrammarExamples.get(G_9);
        BuildContext context = contextCreator.createContext(LALR, grammar, BuildOptions.createEmpty());
        LRAutomaton lr0Automaton = lr0AutomatonBuilder.build(grammar, context);
        LRAutomaton actual = builder.build(lr0Automaton, grammar, context);
    }
}