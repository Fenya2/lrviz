package ru.urfu.lrviz.api.dto;

import java.util.List;

/**
 * @author fenya
 * @since 02.02.2026
 */
public record BuildLogDto(List<BuildOperationDto> operations) {
}
