package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.LRStateDto;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author fenya
 * @since 03.02.2026
 */
@Component
public class LRStateMapper {

    private final ConversionService conversionService;

    public LRStateMapper(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public LRStateDto map(String stateName, LRState state) {
        Set<LRItem> items = state.items();
        List<LRItemDto> convertedItems = new ArrayList<>(items.size());
        for (LRItem item : items) {
            LRItemDto convertedItem = conversionService.convert(item, LRItemDto.class);
            convertedItems.add(convertedItem);
        }
        return new LRStateDto(stateName, convertedItems);
    }
}
