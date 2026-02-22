package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class LRItemConverter implements Converter<LRItem, LRItemDto> {

    private final ConversionService conversionService;

    public LRItemConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public LRItemDto convert(LRItem item) {
        RuleDto convertedRule = conversionService.convert(item.getRule(), RuleDto.class);
        if (item instanceof LR0Item) {
            return new LRItemDto(convertedRule, item.getDotIndex(), null);
        }
        if (item instanceof LR1Item lr1Item) {
            return new LRItemDto(convertedRule, item.getDotIndex(), lr1Item.getLookAheadSymbol().asString());
        }
        throw new UnsupportedOperationException();
    }
}
