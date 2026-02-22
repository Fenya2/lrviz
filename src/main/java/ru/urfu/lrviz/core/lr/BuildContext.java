package ru.urfu.lrviz.core.lr;

import jakarta.annotation.Nullable;
import ru.urfu.lrviz.core.grammar.FirstCalculator;
import ru.urfu.lrviz.core.lr.state.name.generation.StateNameGenerator;

import java.util.Map;

public final class BuildContext {
    private BuildLog buildLog;
    private StateNameGenerator stateNamesGenerator;
    private Map<String, LRState> namedStates;
    private Map<LRAutomaton.TransitionKey, String> definedTransitions;
    @Nullable
    private FirstCalculator firstCalculator;

    public BuildLog getBuildLog() {
        return buildLog;
    }

    public StateNameGenerator getStateNamesGenerator() {
        return stateNamesGenerator;
    }

    public Map<String, LRState> getNamedStates() {
        return namedStates;
    }

    public Map<LRAutomaton.TransitionKey, String> definedTransitions() {
        return definedTransitions;
    }

    @Nullable
    public FirstCalculator getFirstCalculator() {
        return firstCalculator;
    }

    public void setBuildLog(BuildLog buildLog) {
        this.buildLog = buildLog;
    }

    public void setStateNamesGenerator(StateNameGenerator stateNamesGenerator) {
        this.stateNamesGenerator = stateNamesGenerator;
    }

    public void setNamedStates(Map<String, LRState> namedStates) {
        this.namedStates = namedStates;
    }

    public void setDefinedTransitions(Map<LRAutomaton.TransitionKey, String> definedTransitions) {
        this.definedTransitions = definedTransitions;
    }

    public void setFirstCalculator(@Nullable FirstCalculator firstCalculator) {
        this.firstCalculator = firstCalculator;
    }
}
