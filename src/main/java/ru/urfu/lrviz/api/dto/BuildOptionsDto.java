package ru.urfu.lrviz.api.dto;

import jakarta.annotation.Nullable;

/**
 * @author fenya
 * @since 07.03.2026
 */
public record BuildOptionsDto(
        @Nullable String namesGenerationStrategy,
        @Nullable String lalr1BuildAlgorithm,
        @Nullable Boolean enableBuildLog) {
}
