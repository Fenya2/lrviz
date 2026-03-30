package ru.urfu.lrviz.core.lr.lalr1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.*;
import ru.urfu.lrviz.core.lr.lr0.LR0AutomatonBuilder;
import ru.urfu.lrviz.core.lr.lr1.LR1AutomatonBuilder;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;

import java.util.*;
import java.util.stream.Collectors;

import static ru.urfu.lrviz.core.GrammarExamples.*;
import static ru.urfu.lrviz.core.lr.AutomatonType.LALR;
import static ru.urfu.lrviz.core.lr.AutomatonType.LR_1;

@SpringBootTest
class ChannelLALR1AutomatonBuilderTest {
    private final LR0AutomatonBuilder lr0AutomatonBuilder;
    private final BuildContextCreator contextCreator;
    private final ChannelLALR1AutomatonBuilder channelLALR1AutomatonBuilder;

    private final LR1AutomatonBuilder lr1AutomatonBuilder;
    private final ClassicLALR1AutomatonBuilder classicLALR1AutomatonBuilder;

    @Autowired
    ChannelLALR1AutomatonBuilderTest(LR0AutomatonBuilder lr1AutomatonBuilder, BuildContextCreator contextCreator, ChannelLALR1AutomatonBuilder channelLALR1AutomatonBuilder, LR1AutomatonBuilder lr1AutomatonBuilder1, ClassicLALR1AutomatonBuilder classicLALR1AutomatonBuilder) {
        this.lr0AutomatonBuilder = lr1AutomatonBuilder;
        this.contextCreator = contextCreator;
        this.channelLALR1AutomatonBuilder = channelLALR1AutomatonBuilder;
        this.lr1AutomatonBuilder = lr1AutomatonBuilder1;
        this.classicLALR1AutomatonBuilder = classicLALR1AutomatonBuilder;
    }

    @ParameterizedTest
    @ValueSource(strings = {G_1, G_2, G_3, G_4, G_5, G_6, G_7, G_8, G_9})
    void isEqualToAutomatonBuildWithClassicAlgorithm(String grammarName) {
        Grammar grammar = GrammarExamples.get(grammarName);
        BuildContext classicContext = contextCreator.createContext(LR_1, grammar, new BuildOptions(StateNamesGenerationStrategy.END_TO_END_NUMERIC, LALR1BuildAlgorithm.CLASSIC));
        LRAutomaton lr1Automaton = lr1AutomatonBuilder.build(grammar, classicContext);
        LRAutomaton classicLalr = classicLALR1AutomatonBuilder.build(lr1Automaton, classicContext);
        Set<Set<LRItem>> expectedLalrKernels = getExpectedLalr1Kernels(classicLalr, classicContext);

        BuildContext channelContext = contextCreator.createContext(LALR, grammar, new BuildOptions(StateNamesGenerationStrategy.END_TO_END_NUMERIC, LALR1BuildAlgorithm.CHANNEL));
        LRAutomaton lr0Automaton = lr0AutomatonBuilder.build(grammar, channelContext);
        LRAutomaton channelLalr = channelLALR1AutomatonBuilder.build(lr0Automaton, grammar, channelContext);
        Set<Set<LR1Item>> actualLalrKernels = getActualLalr1Kernels(channelLalr, channelContext);
        Assertions.assertEquals(expectedLalrKernels, actualLalrKernels);
    }

    private static Set<Set<LR1Item>> getActualLalr1Kernels(LRAutomaton channelLalr, BuildContext channelContext) {
        Map<String, LRState> channelLalrStates = channelLalr.namedStates();
        Set<Set<LR1Item>> actualLalrKernels = HashSet.newHashSet(channelLalrStates.size());
        for (Map.Entry<String, LRState> namedState : channelLalrStates.entrySet()) {
            Set<LR1Item> unfoldedKernel = getLalr1Kernel(namedState.getValue(), channelContext.getStartItem()).stream()
                    .map(LALR1Item.class::cast)
                    .flatMap(
                            lalr1Item -> lalr1Item.getLookAheadSymbols().stream()
                                    .map(lookAheadSymbol -> new LR1Item(lalr1Item.getRule(), lalr1Item.getDotIndex(), lookAheadSymbol)))
                    .collect(Collectors.toSet());
            actualLalrKernels.add(unfoldedKernel);
        }
        return actualLalrKernels;
    }

    private Set<Set<LRItem>> getExpectedLalr1Kernels(LRAutomaton classicLalr, BuildContext classicContext) {
        Map<String, LRState> classicLalr1States = classicLalr.namedStates();
        Set<Set<LRItem>> expectedLalr1Kernels = HashSet.newHashSet(classicLalr1States.size());
        for (Map.Entry<String, LRState> namedState : classicLalr1States.entrySet()) {
            Set<LRItem> kernel = getLalr1Kernel(namedState.getValue(), classicContext.getStartItem());
            expectedLalr1Kernels.add(kernel);
        }
        return expectedLalr1Kernels;
    }

    private static Set<LRItem> getLalr1Kernel(LRState state, LRItem startItem) {
        return state.items().stream()
                .filter(item -> equalsByBasePart(startItem, item) || !item.isDotSymbolAtTheBeginning())
                .collect(Collectors.toSet());
    }

    private static boolean equalsByBasePart(LRItem first, LRItem second) {
        return first.getRule().equals(second.getRule()) && first.getDotIndex() == second.getDotIndex();
    }
}