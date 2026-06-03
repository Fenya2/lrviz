package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.operations.AddLookAheadOperationDto;
import ru.urfu.lrviz.core.lr.operations.AddLookAheadOperation;

/**
 *
 * @author fenya
 * @since 30.03.2026
 */
@Component
public class AddLookAheadOperationConverter implements Converter<AddLookAheadOperation, AddLookAheadOperationDto> {

    private final ConversionService conversionService;

    public AddLookAheadOperationConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public AddLookAheadOperationDto convert(AddLookAheadOperation operation) {
        LRItemDto convertedItem = conversionService.convert(operation.item, LRItemDto.class);
        return new AddLookAheadOperationDto(
                operation.message, operation.stateName, convertedItem, operation.lookAheadSymbol.asString());
    }
}
