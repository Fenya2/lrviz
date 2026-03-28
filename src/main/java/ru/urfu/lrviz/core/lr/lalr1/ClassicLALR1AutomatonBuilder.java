package ru.urfu.lrviz.core.lr.lalr1;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.BuildLogUtils;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRState;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;
import ru.urfu.lrviz.core.lr.operations.CompactLRAutomatonOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Строит LALR-1 автомат по {@link LALR1BuildAlgorithm#CLASSIC классическому алгоритму}
 *
 * @author fenya
 * @since 14.03.2026
 */
@Component
public class ClassicLALR1AutomatonBuilder {
    /**
     * @param lr1Automaton LR(1) автомат, который ужимается до LALR(1) автомата
     * @param context контекст построения
     * @return LALR(1) автомат
     */
    public LRAutomaton build(LRAutomaton lr1Automaton, BuildContext context) {
        context.getBuildLog().append(new CompactLRAutomatonOperation());
        Map<String, LRState> lr1States = lr1Automaton.namedStates();
        Map<String, Set<LR0Item>> kernelSets = createKernelSets(lr1States);
        Map<LRAutomaton.TransitionKey, String> lr1Transitions = lr1Automaton.transitions();
        Map<Set<LR0Item>, Set<String>> groupedByKernel = groupByKernelSets(kernelSets);

        Map<String, String> newStateNames = getRenamedStates(context, lr1States, groupedByKernel);
        Map<String, LRState> lalr1States = mergeStates(lr1States, newStateNames);
        Map<LRAutomaton.TransitionKey, String> lalr1Transitions = updateTransitions(lr1Transitions, newStateNames);
        log(context, lr1Transitions, newStateNames, lalr1Transitions);
        return new LRAutomaton(lalr1States, lalr1Transitions);
    }

    /**
     * @param namedStates именованные состояние lr(1)-автомата
     * @return ядра для каждого состояния lr(1)-автомата
     * @implNote ядром состояния lr(1)-автомата здесь называется множество lr(0)-пунктов, полученное отбрасыванием
     * символов предпросмотра у lr(1)-пунктов у соответствующего состояния
     */
    private Map<String, Set<LR0Item>> createKernelSets(Map<String, LRState> namedStates) {
        return namedStates.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> getKernels(entry.getValue())
        ));
    }

    Set<LR0Item> getKernels(LRState state) {
        return state.items().stream()
                .map(item -> new LR0Item(item.getRule(), item.getDotIndex())).collect(Collectors.toSet());
    }

    /**
     * Группирует ядра lr(1)-автомата. Равные ядра попадают в одну группу
     *
     * @param kernelSets ядра lr(1)-автомата
     * @return сгрупированные ядра. Ключом является ядро, значением - имена состояний, ядра которых равны ключу
     */
    private Map<Set<LR0Item>, Set<String>> groupByKernelSets(Map<String, Set<LR0Item>> kernelSets) {
        Map<Set<LR0Item>, Set<String>> groupedByKernel = new HashMap<>();
        for (Map.Entry<String, Set<LR0Item>> entry : kernelSets.entrySet()) {
            Set<LR0Item> value = entry.getValue();
            groupedByKernel.computeIfAbsent(value, _ -> new HashSet<>());
            groupedByKernel.get(value).add(entry.getKey());
        }
        return groupedByKernel;
    }

    /**
     * Формирует имена состояний после их слияния. Возвращает словарь, где ключом является имя состояние, которое
     * подверглось слиянию, значением - имя нового состояния, полученное слиянием состояния с именем ключа
     *
     * @param context         контекст построения автомата
     * @param lr1States       именованные состояния lr(1)-автомата
     * @param groupedByKernel имена, состояний, сгрупированные по отношению "Ядро состояния 1 равно ядру состояния 2"
     * @return словарь переименованных состояний
     */
    private static Map<String, String> getRenamedStates(BuildContext context, Map<String, LRState> lr1States, Map<Set<LR0Item>, Set<String>> groupedByKernel) {
        Map<String, String> newStateNames = HashMap.newHashMap(lr1States.size());
        for (String oldStateName : lr1States.keySet()) {
            for (Set<String> groupedByKernelStateNames : groupedByKernel.values()) {
                if (groupedByKernelStateNames.size() > 1 && groupedByKernelStateNames.contains(oldStateName)) {
                    newStateNames.put(oldStateName, context.getStateNamesGenerator().generateForMergingStates(groupedByKernelStateNames));
                    break;
                }
            }
        }
        return newStateNames;
    }

    /**
     * Выполняет слияние состояний lr(1)-автомата
     *
     * @param lr1States     состояния lr(1)-автомата
     * @param newStateNames словарь переименований состояний
     * @return именованные lalr(1)-состояния
     */
    private static Map<String, LRState> mergeStates(Map<String, LRState> lr1States, Map<String, String> newStateNames) {
        Map<String, LRState> lalr1States = new HashMap<>();
        for (Map.Entry<String, LRState> lr1State : lr1States.entrySet()) {
            String oldStateName = lr1State.getKey();
            String newStateName = newStateNames.get(oldStateName);
            if (newStateName == null) {
                lalr1States.put(oldStateName, lr1State.getValue());
                continue;
            }
            lalr1States.compute(newStateName,
                    (_, value) -> value == null ? lr1State.getValue() : LRState.merge(value, lr1State.getValue()));
        }
        return lalr1States;
    }

    /**
     * Формирует переходы lalr(1)-состояния после слияния состояний lr(1)-автомата с общим ядром
     *
     * @param lr1Transitions переходы lr(1)-автомата
     * @param newStateNames  новые имена состояний
     * @return переходы lalr(1)-автомата
     */
    private static Map<LRAutomaton.TransitionKey, String> updateTransitions(Map<LRAutomaton.TransitionKey, String> lr1Transitions, Map<String, String> newStateNames) {
        Map<LRAutomaton.TransitionKey, String> lalr1Transitions = HashMap.newHashMap(lr1Transitions.size());
        for (Map.Entry<LRAutomaton.TransitionKey, String> lr1Transition : lr1Transitions.entrySet()) {
            LRAutomaton.TransitionKey transitionKey = lr1Transition.getKey();
            String fromStateName = transitionKey.stateName();
            String toStateName = lr1Transition.getValue();
            String newFromStateName = newStateNames.getOrDefault(fromStateName, fromStateName);
            String newToStateName = newStateNames.getOrDefault(toStateName, toStateName);
            lalr1Transitions.put(new LRAutomaton.TransitionKey(newFromStateName, transitionKey.symbol()), newToStateName);
        }
        return lalr1Transitions;
    }

    /**
     * Логирует операции, приводящие к построению lalr(1)-алгоритма
     */
    private static void log(BuildContext context, Map<LRAutomaton.TransitionKey, String> lr1Transitions, Map<String, String> newStateNames, Map<LRAutomaton.TransitionKey, String> lalr1Transitions) {
        Set<Map.Entry<LRAutomaton.TransitionKey, String>> transitionsToRemove = lr1Transitions.entrySet().stream()
                .filter(entry -> newStateNames.containsKey(entry.getKey().stateName()) || newStateNames.containsKey(entry.getValue()))
                .collect(Collectors.toSet());
        BuildLogUtils.logTransitionsDeletions(transitionsToRemove, context.getBuildLog());
        BuildLogUtils.logStatesDeletions(newStateNames.keySet(), context.getBuildLog());

        HashSet<String> mergedStateNames = new HashSet<>(newStateNames.values());
        BuildLogUtils.logLRStatesAdditions(mergedStateNames, context.getBuildLog());
        Set<Map.Entry<LRAutomaton.TransitionKey, String>> transitionsToAdd = lalr1Transitions.entrySet().stream()
                .filter(entry -> mergedStateNames.contains(entry.getKey().stateName()) || mergedStateNames.contains(entry.getValue()))
                .collect(Collectors.toSet());
        BuildLogUtils.logTransitionsAdditions(transitionsToAdd, context.getBuildLog());
    }
}
