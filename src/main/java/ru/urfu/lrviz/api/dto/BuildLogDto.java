package ru.urfu.lrviz.api.dto;

import ru.urfu.lrviz.api.dto.operations.BuildOperationDto;

import java.util.List;

/**
 * @author fenya
 * @since 02.02.2026
 */
public record BuildLogDto(List<BuildOperationDto> operations) {
}
