package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.operations.AddItemInStateOperationDto;
import ru.urfu.lrviz.core.lr.operations.AddItemInStateOperation;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class AddItemInStateOperationConverter implements Converter<AddItemInStateOperation, AddItemInStateOperationDto> {

    private final ConversionService conversionService;

    public AddItemInStateOperationConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public AddItemInStateOperationDto convert(AddItemInStateOperation source) {
        return new AddItemInStateOperationDto(
                source.message, source.stateName, conversionService.convert(source.item, LRItemDto.class));
    }
}
