package ru.urfu.lrviz.api;

import ru.urfu.lrviz.api.dto.BuildLogDto;
import ru.urfu.lrviz.api.dto.LRAutomatonDto;

/**
 * Результат построения LR-автомата
 *
 * @param automaton автомат
 * @param buildLog  лог построения
 *
 * @author fenya
 * @since 02.02.2026
 */
public record LrBuildResultDto(LRAutomatonDto automaton, BuildLogDto buildLog) {
}
