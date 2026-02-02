package ru.urfu.lrviz.core.lr;

import ru.urfu.lrviz.core.lr.state.name.generation.ByTransitionSymbolStateNameGenerator;
import ru.urfu.lrviz.core.lr.state.name.generation.StateNameGenerator;

import java.util.HashMap;
import java.util.Map;

public record BuildContext(BuildLog buildLog,
                           StateNameGenerator stateNamesCounter,
                           Map<String, LRState> namedStates,
                           Map<LRAutomaton.TransitionKey, String> definedTransitions) {

    public static BuildContext create() {
        return new BuildContext(new BuildLog(), new ByTransitionSymbolStateNameGenerator(), new HashMap<>(), new HashMap<>());
    }
}
