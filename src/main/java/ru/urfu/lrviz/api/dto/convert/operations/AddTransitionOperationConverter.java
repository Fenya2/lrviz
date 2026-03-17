package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.operations.AddTransitionOperationDto;
import ru.urfu.lrviz.core.lr.operations.AddTransitionOperation;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
@Component
public class AddTransitionOperationConverter implements Converter<AddTransitionOperation, AddTransitionOperationDto> {
    @Override
    public AddTransitionOperationDto convert(AddTransitionOperation source) {
        return new AddTransitionOperationDto(source.message, source.from, source.to, source.through.asString());
    }
}
