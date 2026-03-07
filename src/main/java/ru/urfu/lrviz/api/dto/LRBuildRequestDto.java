package ru.urfu.lrviz.api.dto;

import jakarta.annotation.Nullable;

/**
 * Запрос на построение LR-автомата
 *
 * @author fenya
 * @since 07.03.2026
 */
public record LRBuildRequestDto(GrammarDto grammar, @Nullable BuildOptionsDto buildOptions) {
}
