package ru.urfu.lrviz.core.lr;

import jakarta.annotation.Nullable;
import ru.urfu.lrviz.core.grammar.FirstCalculator;
import ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm;
import ru.urfu.lrviz.core.lr.state.name.generation.StateNameGenerator;

import java.util.Map;
import java.util.Set;

public final class BuildContext {
    /**
     * Лог построения автомата
     */
    private final BuildLog buildLog;
    /**
     * Генератор имен состояний, использующийся при построении автомата
     */
    private final StateNameGenerator stateNamesGenerator;
    /**
     * Состояния и их имена, которые будут заполняться в процессе построения автомата
     */
    private final Map<String, LRState> namedStates;
    /**
     * Переходы между состояниями автомата, которые будут заполняться в процессе построения
     */
    private final Map<TransitionKey, String> definedTransitions;

    /**
     * Список переходов, через которые очередное состояние добавлялось в LR(0/1)-автомат впервые
     */
    private final Set<TransitionEntry> originTransitions;

    @Nullable
    private FirstCalculator firstCalculator;

    @Nullable
    private LALR1BuildAlgorithm lalr1BuildAlgorithm;

    /**
     * Имя начального состояния
     */
    private String startStateName;

    /**
     * Начальный пункт
     */
    private LRItem startItem;

    public BuildContext(BuildLog buildLog, StateNameGenerator stateNamesGenerator, Map<String, LRState> namedStates, Map<TransitionKey, String> definedTransitions, Set<TransitionEntry> originTransitions) {
        this.buildLog = buildLog;
        this.stateNamesGenerator = stateNamesGenerator;
        this.namedStates = namedStates;
        this.definedTransitions = definedTransitions;
        this.originTransitions = originTransitions;
    }

    public BuildLog getBuildLog() {
        return buildLog;
    }

    public StateNameGenerator getStateNamesGenerator() {
        return stateNamesGenerator;
    }

    public Map<String, LRState> getNamedStates() {
        return namedStates;
    }

    public Map<TransitionKey, String> getDefinedTransitions() {
        return definedTransitions;
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

    public LRItem getStartItem() {
        return startItem;
    }

    public void setStartItem(LRItem startItem) {
        this.startItem = startItem;
    }

    public Set<TransitionEntry> getOriginTransitions() {
        return originTransitions;
    }

    public String getStartStateName() {
        return startStateName;
    }

    public void setStartStateName(String startStateName) {
        this.startStateName = startStateName;
    }
}
