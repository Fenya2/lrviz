package ru.urfu.lrviz.render;

import jakarta.annotation.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fenya
 * @since 28.02.2026
 */
public final class VisualizeOptions {
    private final boolean colorizeTransitions;
    private final boolean highLightBaseItems;
    private final boolean colorizeStateNames;
    private final StateNameStyle stateNameStyle;

    @Nullable
    private final Set<Integer> visualizeOperations;

    public static class VisualizeParametersBuilder {
        private boolean highLightBaseItems = false;
        private boolean colorizeTransitions = false;
        private boolean colorizeStateNames = false;
        private StateNameStyle stateNameStyle = StateNameStyle.ON_BLACK_BACKGROUND;
        private List<Integer> visualizeOperations;

        private VisualizeParametersBuilder() {
        }

        public VisualizeParametersBuilder highLightBaseItems(boolean highLightBaseItems) {
            this.highLightBaseItems = highLightBaseItems;
            return this;
        }

        public VisualizeParametersBuilder colorizeTransitions(boolean colorizeTransitions) {
            this.colorizeTransitions = colorizeTransitions;
            return this;
        }

        public VisualizeParametersBuilder colorizeStateNames(boolean colorizeStateNames) {
            this.colorizeStateNames = colorizeStateNames;
            return this;
        }

        public VisualizeParametersBuilder visualizeSpecifiedBuildLogOperations(List<Integer> visualizeOperationsNumbers) {
            this.visualizeOperations = visualizeOperationsNumbers;
            return this;
        }

        public VisualizeParametersBuilder stateNameStyle(StateNameStyle stateNameStyle) {
            this.stateNameStyle = stateNameStyle;
            return this;
        }

        public VisualizeOptions build() {
            return new VisualizeOptions(colorizeTransitions, highLightBaseItems, colorizeStateNames, stateNameStyle, visualizeOperations);
        }
    }

    /**
     * @param colorizeTransitions окрашивать ли переходы в разные цвета
     * @param highLightBaseItems  подсвечивать ли базовые пункты автомата
     * @param stateNameStyle      стиль оформления названия состояния
     * @param visualizeOperations номера операций, которые нужно визуализировать
     */
    private VisualizeOptions(boolean colorizeTransitions, boolean highLightBaseItems, boolean colorizeStateNames, StateNameStyle stateNameStyle, @Nullable List<Integer> visualizeOperations) {
        this.colorizeTransitions = colorizeTransitions;
        this.highLightBaseItems = highLightBaseItems;
        this.colorizeStateNames = colorizeStateNames;
        this.stateNameStyle = stateNameStyle;
        this.visualizeOperations = visualizeOperations == null ? null : new HashSet<>(visualizeOperations);
    }

    public static VisualizeOptions createDefault() {
        return builder().build();
    }

    public static VisualizeParametersBuilder builder() {
        return new VisualizeParametersBuilder();
    }


    public boolean isHighLightBaseItems() {
        return highLightBaseItems;
    }

    public boolean isColorizeTransitions() {
        return colorizeTransitions;
    }

    public boolean isColorizeStateNames() {
        return colorizeStateNames;
    }

    public StateNameStyle getStateNameStyle() {
        return stateNameStyle;
    }

    @Nullable
    public Set<Integer> getVisualizeOperations() {
        return visualizeOperations;
    }
}
