package ru.urfu.lrviz.api.dto;

import java.util.List;

public record GrammarDto(
        List<String> terminals,
        List<String> nonTerminals,
        List<RuleDto> rules,
        String startSymbol) {
}
