package ru.urfu.lrviz.core.lr;

import jakarta.annotation.Nullable;
import ru.urfu.lrviz.core.grammar.FirstCalculator;
import ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm;
import ru.urfu.lrviz.core.lr.state.name.generation.StateNameGenerator;

import java.util.Map;

public final class BuildContext {
    /**
     * Лош построения автомата
     */
    private BuildLog buildLog;
    /**
     * Генератор имен состояний, использующийся при построении автомата
     */
    private StateNameGenerator stateNamesGenerator;
    /**
     * Состояния и их имена, которые будут заполняться в процессе построения автомата
     */
    private Map<String, LRState> namedStates;
    /**
     * Переходы между состояниями автомата, которые будут заполняться в процессе построения
     */
    private Map<LRAutomaton.TransitionKey, String> definedTransitions;
    @Nullable
    private FirstCalculator firstCalculator;
    @Nullable
    private LALR1BuildAlgorithm lalr1BuildAlgorithm;

    public BuildLog getBuildLog() {
        return buildLog;
    }

    public void setBuildLog(BuildLog buildLog) {
        this.buildLog = buildLog;
    }

    public StateNameGenerator getStateNamesGenerator() {
        return stateNamesGenerator;
    }

    public void setStateNamesGenerator(StateNameGenerator stateNamesGenerator) {
        this.stateNamesGenerator = stateNamesGenerator;
    }

    public Map<String, LRState> getNamedStates() {
        return namedStates;
    }

    public void setNamedStates(Map<String, LRState> namedStates) {
        this.namedStates = namedStates;
    }

    public Map<LRAutomaton.TransitionKey, String> getDefinedTransitions() {
        return definedTransitions;
    }

    public void setDefinedTransitions(Map<LRAutomaton.TransitionKey, String> definedTransitions) {
        this.definedTransitions = definedTransitions;
    }

    @Nullable
    public FirstCalculator getFirstCalculator() {
        return firstCalculator;
    }

    public void setFirstCalculator(@Nullable FirstCalculator firstCalculator) {
        this.firstCalculator = firstCalculator;
    }

    @Nullable
    public LALR1BuildAlgorithm getLalr1BuildAlgorithm() {
        return lalr1BuildAlgorithm;
    }

    public void setLalr1BuildAlgorithm(@Nullable LALR1BuildAlgorithm lalr1BuildAlgorithm) {
        this.lalr1BuildAlgorithm = lalr1BuildAlgorithm;
    }
}
