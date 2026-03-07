package ru.urfu.lrviz.core.lr;

import jakarta.annotation.Nullable;

/**
 * Параметры построения LR-автомата
 *
 * @author fenya
 * @since 07.03.2026
 */
public record BuildOptions(@Nullable StateNamesGenerationStrategy namesGenerationStrategy) {
    public static final BuildOptions EMPTY_CONTEXT = new BuildOptions(null);

    public static BuildOptions createEmpty() {
        return EMPTY_CONTEXT;
    }
}
