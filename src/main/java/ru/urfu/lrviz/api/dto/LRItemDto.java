package ru.urfu.lrviz.api.dto;

import jakarta.annotation.Nullable;

import java.util.List;

public record LRItemDto(
        RuleDto rule,
        int dotIndex,
        @Nullable String lookAheadSymbol,
        @Nullable List<String> lookAheadSymbols
) {
}
