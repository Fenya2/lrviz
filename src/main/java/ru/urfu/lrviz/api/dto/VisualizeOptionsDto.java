package ru.urfu.lrviz.api.dto;

import jakarta.annotation.Nullable;

import java.util.List;

/**
 * Параметры визуализации построенного LR-автомата
 *
 * @author fenya
 * @since 05.04.2026
 */
public record VisualizeOptionsDto(@Nullable List<Integer> visualizeOperations) {
}
