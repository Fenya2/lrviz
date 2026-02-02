package ru.urfu.lrviz.core.lr.lrnew;

import ru.urfu.lrviz.core.lr.BuildLog;

import java.util.HashMap;
import java.util.Map;

public record BuildContext(BuildLog buildLog,
                           Map<String, LRState> namedStates,
                           Map<LRAutomaton.TransitionKey, String> definedTransitions) {

    public static BuildContext create() {
        return new BuildContext(new BuildLog(), new HashMap<>(), new HashMap<>());
    }
}
