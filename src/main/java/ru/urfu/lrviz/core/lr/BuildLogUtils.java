package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.lr.operations.*;

import java.util.Map;
import java.util.Set;

/**
 * @author fenya
 * @since 17.03.2026
 */
public class BuildLogUtils {
    /**
     * Добавляет в лог {@code log} операции добавления пунктов {@code newItems} в состояние с именем {@code stateName}
     */
    public static void logNewItemsAddition(String stateName, Set<LRItem> newItems, BuildLog log) {
        for (LRItem item : newItems) {
            log.append(new AddItemInStateOperation(stateName, item));
        }
    }

    public static void logTransitionsDeletions(Set<Map.Entry<LRAutomaton.TransitionKey, String>> transitions, BuildLog log) {
        for (Map.Entry<LRAutomaton.TransitionKey, String> transition : transitions) {
            LRAutomaton.TransitionKey transitionKey = transition.getKey();
            log.append(new DeleteTransitionOperation(transitionKey.stateName(), transition.getValue(), transitionKey.symbol()));
        }
    }

    public static void logTransitionsAdditions(Set<Map.Entry<LRAutomaton.TransitionKey, String>> transitions, BuildLog log) {
        for (Map.Entry<LRAutomaton.TransitionKey, String> transition : transitions) {
            LRAutomaton.TransitionKey transitionKey = transition.getKey();
            log.append(new AddTransitionOperation(transitionKey.stateName(), transition.getValue(), transitionKey.symbol()));
        }
    }


    public static void logStatesDeletions(Set<String> stateNames, BuildLog log) {
        for (String stateName : stateNames) {
            log.append(new DeleteStateOperation(stateName));
        }
    }

    private BuildLogUtils() {
    }
}
