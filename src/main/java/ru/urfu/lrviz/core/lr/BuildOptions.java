package ru.urfu.lrviz.core.lr;

import jakarta.annotation.Nullable;
import ru.urfu.lrviz.core.lr.lalr1.LALR1BuildAlgorithm;

/**
 * Параметры построения LR-автомата
 *
 * @author fenya
 * @since 07.03.2026
 */
public record BuildOptions(@Nullable StateNamesGenerationStrategy namesGenerationStrategy,
                           @Nullable LALR1BuildAlgorithm lalr1BuildAlgorithm) {
    public static final BuildOptions EMPTY_CONTEXT = new BuildOptions(null, null);

    public static BuildOptions createEmpty() {
        return EMPTY_CONTEXT;
    }
}
