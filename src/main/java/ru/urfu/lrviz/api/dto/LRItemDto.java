package ru.urfu.lrviz.api.dto;

import jakarta.annotation.Nullable;

public record LRItemDto(
        RuleDto rule,
        int dotIndex,
        @Nullable String lookAheadSymbol
) {
}
