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
    private final RenderFormat format;
    private final boolean highLightBaseItems;
    @Nullable
    private final Set<Integer> visualizeOperations;

    public static class VisualizeParametersBuilder {
        private RenderFormat renderFormat = RenderFormat.PNG;
        private boolean highLightBaseItems = false;
        private List<Integer> visualizeOperations;

        private VisualizeParametersBuilder() {
        }

        public VisualizeParametersBuilder renderFormat(RenderFormat renderFormat) {
            this.renderFormat = renderFormat;
            return this;
        }

        public VisualizeParametersBuilder highLightBaseItems(boolean highLightBaseItems) {
            this.highLightBaseItems = highLightBaseItems;
            return this;
        }

        public VisualizeParametersBuilder visualizeSpecifiedBuildLogOperations(List<Integer> visualizeOperationsNumbers) {
            this.visualizeOperations = visualizeOperationsNumbers;
            return this;
        }

        public VisualizeOptions build() {
            return new VisualizeOptions(renderFormat, highLightBaseItems, visualizeOperations);
        }
    }

    /**
     * @param format              формат изображения
     * @param highLightBaseItems  подсвечивать ли базовые пункты автомата
     * @param visualizeOperations номера операций, которые нужно визуализировать
     */
    private VisualizeOptions(RenderFormat format, boolean highLightBaseItems, @Nullable List<Integer> visualizeOperations) {
        this.format = format;
        this.highLightBaseItems = highLightBaseItems;
        this.visualizeOperations = visualizeOperations == null ? null : new HashSet<>(visualizeOperations);
    }

    public static VisualizeOptions createDefault() {
        return builder().build();
    }

    public static VisualizeParametersBuilder builder() {
        return new VisualizeParametersBuilder();
    }

    public RenderFormat getFormat() {
        return format;
    }

    public boolean highLightBaseItems() {
        return highLightBaseItems;
    }

    @Nullable
    public Set<Integer> getVisualizeOperations() {
        return visualizeOperations;
    }
}
