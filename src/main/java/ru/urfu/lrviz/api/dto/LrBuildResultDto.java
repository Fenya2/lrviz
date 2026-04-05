package ru.urfu.lrviz.api.dto;

import jakarta.annotation.Nullable;

/**
 * Результат построения LR-автомата
 *
 * @param automaton автомат
 * @param buildLog  лог построения
 * @author fenya
 * @since 02.02.2026
 */
public record LrBuildResultDto(LRAutomatonDto automaton, @Nullable BuildLogDto buildLog) {
}
