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
    @Nullable
    private final Set<Integer> visualizeOperations;

    public static class VisualizeParametersBuilder {
        private boolean highLightBaseItems = false;
        private boolean colorizeTransitions = false;
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

        public VisualizeParametersBuilder visualizeSpecifiedBuildLogOperations(List<Integer> visualizeOperationsNumbers) {
            this.visualizeOperations = visualizeOperationsNumbers;
            return this;
        }

        public VisualizeOptions build() {
            return new VisualizeOptions(colorizeTransitions, highLightBaseItems, visualizeOperations);
        }
    }

    /**
     * @param colorizeTransitions окрашивать ли переходы в разные цвета
     * @param highLightBaseItems  подсвечивать ли базовые пункты автомата
     * @param visualizeOperations номера операций, которые нужно визуализировать
     */
    private VisualizeOptions(boolean colorizeTransitions, boolean highLightBaseItems, @Nullable List<Integer> visualizeOperations) {
        this.colorizeTransitions = colorizeTransitions;
        this.highLightBaseItems = highLightBaseItems;
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

    @Nullable
    public Set<Integer> getVisualizeOperations() {
        return visualizeOperations;
    }
}
