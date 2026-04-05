package ru.urfu.lrviz.core.lr.lalr1;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.urfu.lrviz.core.GrammarExamples.*;
import static ru.urfu.lrviz.core.lr.AutomatonType.LALR;
import static ru.urfu.lrviz.core.lr.AutomatonType.LR_1;

@SpringBootTest
class ChannelLALR1AutomatonBuilderTest {
    public static final String CHECK_TRANSITIONS_COUNT_MESSAGE = "Количество переходов из ядра '%s' в автомате, построенном по классическому алгоритму, должно совпадать с количеством переходов из ядра '%s' в автомате, построенном канальным алгоритмом";
    private final LR0AutomatonBuilder lr0AutomatonBuilder;
    private final BuildContextCreator contextCreator;
    private final ChannelLALR1AutomatonBuilder channelLALR1AutomatonBuilder;

    private final LR1AutomatonBuilder lr1AutomatonBuilder;
    private final ClassicLALR1AutomatonBuilder classicLALR1AutomatonBuilder;

    @Autowired
    ChannelLALR1AutomatonBuilderTest(LR0AutomatonBuilder lr1AutomatonBuilder, BuildContextCreator contextCreator, ChannelLALR1AutomatonBuilder channelLALR1AutomatonBuilder, LR1AutomatonBuilder lr1AutomatonBuilder1, ClassicLALR1AutomatonBuilder classicLALR1AutomatonBuilder, LRAutomatonReconstructor reconstructor) {
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
        BuildOptions classicOptions = BuildOptions.builder()
                .namesGenerationStrategy(StateNamesGenerationStrategy.END_TO_END_NUMERIC)
                .lalr1BuildAlgorithm(LALR1BuildAlgorithm.CLASSIC)
                .build();
        BuildContext classicContext = contextCreator.createContext(LR_1, grammar, classicOptions);
        LRAutomaton lr1Automaton = lr1AutomatonBuilder.build(grammar, classicContext);
        LRAutomaton classicLalr = classicLALR1AutomatonBuilder.build(lr1Automaton, classicContext);
        Map<String, Set<LRItem>> expectedLalrKernels = getExpectedLalr1Kernels(classicLalr, classicContext);

        BuildOptions channelOptions = BuildOptions.builder()
                .namesGenerationStrategy(StateNamesGenerationStrategy.END_TO_END_NUMERIC)
                .lalr1BuildAlgorithm(LALR1BuildAlgorithm.CHANNEL)
                .build();
        BuildContext channelContext = contextCreator.createContext(LALR, grammar, channelOptions);
        LRAutomaton lr0Automaton = lr0AutomatonBuilder.build(grammar, channelContext);
        LRAutomaton channelLalr = channelLALR1AutomatonBuilder.build(lr0Automaton, grammar, channelContext);
        Map<String, Set<LRItem>> actualLalrKernels = getActualLalr1Kernels(channelLalr, channelContext);
        assertEquals(new HashSet<>(expectedLalrKernels.values()), new HashSet<>(actualLalrKernels.values()));
        assertTransitionsEqual(classicLalr, classicContext, expectedLalrKernels, channelLalr, actualLalrKernels);
    }

    private static void assertTransitionsEqual(LRAutomaton classicLalr, BuildContext classicContext, Map<String, Set<LRItem>> expectedLalrKernels, LRAutomaton channelLalr, Map<String, Set<LRItem>> actualLalrKernels) {
        Queue<Set<LRItem>> processingKernels = new ArrayDeque<>(Collections.singleton(Collections.singleton(classicContext.getStartItem())));
        Set<Set<LRItem>> processedKernels = new HashSet<>();
        while (!processingKernels.isEmpty()) {
            Set<LRItem> checkingKernel = processingKernels.poll();
            if (processedKernels.contains(checkingKernel)) {
                continue;
            }
            String kernelNameInExpected = findKernelName(expectedLalrKernels, checkingKernel);
            String kernelNameInActual = findKernelName(actualLalrKernels, checkingKernel);

            Set<Map.Entry<LRAutomaton.TransitionKey, String>> transitionsFromExpectedKernel = getTransitionsFromKernel(classicLalr, kernelNameInExpected);
            Set<Map.Entry<LRAutomaton.TransitionKey, String>> transitionsFromActualKernel = getTransitionsFromKernel(channelLalr, kernelNameInActual);
            assertEquals(transitionsFromExpectedKernel.size(), transitionsFromActualKernel.size(), CHECK_TRANSITIONS_COUNT_MESSAGE.formatted(kernelNameInExpected, kernelNameInActual));

            for (Map.Entry<LRAutomaton.TransitionKey, String> transitionInExpectedKernel : transitionsFromExpectedKernel) {
                TransitionSymbol transitionSymbol = transitionInExpectedKernel.getKey().symbol();
                Map.Entry<LRAutomaton.TransitionKey, String> transitionWithSameSymbolInActualKernel = transitionsFromActualKernel.stream()
                        .filter(actualTransition -> transitionSymbol.equals(actualTransition.getKey().symbol()))
                        .findFirst()
                        .orElseThrow();
                Set<LRItem> targetKernelInExpected = expectedLalrKernels.get(transitionInExpectedKernel.getValue());
                Set<LRItem> targetKernelInActual = actualLalrKernels.get(transitionWithSameSymbolInActualKernel.getValue());

                assertEquals(targetKernelInExpected, targetKernelInActual);
                processingKernels.add(targetKernelInExpected);
                processingKernels.add(targetKernelInActual);
            }
            processedKernels.add(checkingKernel);
        }
    }

    private static Set<Map.Entry<LRAutomaton.TransitionKey, String>> getTransitionsFromKernel(LRAutomaton classicLalr, String kernelName) {
        return classicLalr.transitions().entrySet().stream()
                .filter(transition -> kernelName.equals(transition.getKey().stateName()))
                .collect(Collectors.toSet());
    }

    private static String findKernelName(Map<String, Set<LRItem>> expectedLalrKernels, Set<LRItem> checkingKernel) {
        return expectedLalrKernels.entrySet().stream()
                .filter(entry -> checkingKernel.equals(entry.getValue()))
                .map(Map.Entry::getKey).findFirst().orElseThrow();
    }

    private static Map<String, Set<LRItem>> getActualLalr1Kernels(LRAutomaton channelLalr, BuildContext channelContext) {
        Map<String, LRState> channelLalrStates = channelLalr.namedStates();
        Map<String, Set<LRItem>> actualLalrKernels = HashMap.newHashMap(channelLalrStates.size());
        for (Map.Entry<String, LRState> namedState : channelLalrStates.entrySet()) {
            Set<LRItem> unfoldedKernel = getLalr1Kernel(namedState.getValue(), channelContext.getStartItem()).stream()
                    .map(LALR1Item.class::cast)
                    .flatMap(
                            lalr1Item -> lalr1Item.getLookAheadSymbols().stream()
                                    .map(lookAheadSymbol -> new LR1Item(lalr1Item.getRule(), lalr1Item.getDotIndex(), lookAheadSymbol)))
                    .collect(Collectors.toSet());
            actualLalrKernels.put(namedState.getKey(), unfoldedKernel);
        }
        return actualLalrKernels;
    }

    private Map<String, Set<LRItem>> getExpectedLalr1Kernels(LRAutomaton classicLalr, BuildContext classicContext) {
        Map<String, LRState> classicLalr1States = classicLalr.namedStates();
        Map<String, Set<LRItem>> expectedLalr1Kernels = HashMap.newHashMap(classicLalr1States.size());
        for (Map.Entry<String, LRState> namedState : classicLalr1States.entrySet()) {
            Set<LRItem> kernel = getLalr1Kernel(namedState.getValue(), classicContext.getStartItem());
            expectedLalr1Kernels.put(namedState.getKey(), kernel);
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