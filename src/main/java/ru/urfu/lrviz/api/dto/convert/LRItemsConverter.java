package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.lr.LR0Item;
import ru.urfu.lrviz.core.lr.lrnew.LRItem;

import java.util.stream.Collectors;

public class LRItemsConverter implements Converter<LRItem, LRItemDto> {

    @Override
    public LRItemDto convert(LRItem source) {
        if (source instanceof LR0Item lr0Item) {
            RuleDto rule = new RuleDto(lr0Item.getRule().left().lexicalValue, lr0Item.getRule().right().stream().map(s -> s.lexicalValue).collect(Collectors.joining()));
            return new LRItemDto(rule, lr0Item.dotIndex(), null);
        }
        throw new IllegalArgumentException("Type of Item is not supported.");
    }
}
