package ru.urfu.lrviz.core.lr;

import java.util.HashMap;
import java.util.Map;

public record BuildContext(BuildLog buildLog,
                           StateNamesCounter stateNamesCounter,
                           Map<String, LRState> namedStates,
                           Map<LRAutomaton.TransitionKey, String> definedTransitions) {

    public static BuildContext create() {
        return new BuildContext(new BuildLog(), new StateNamesCounter(), new HashMap<>(), new HashMap<>());
    }
}
