package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import ru.urfu.lrviz.api.dto.operations.AddStateOperationDto;
import ru.urfu.lrviz.core.lr.operations.AddStateOperation;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public class AddStateOperationConverter implements Converter<AddStateOperation, AddStateOperationDto> {
    @Override
    public AddStateOperationDto convert(AddStateOperation source) {
        // TODO;
        return null;
    }
}
