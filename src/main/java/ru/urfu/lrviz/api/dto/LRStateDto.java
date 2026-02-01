package ru.urfu.lrviz.api.dto;

import java.util.List;

public record LRStateDto(String name, List<LRItemDto> items) {
}
