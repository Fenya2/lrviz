package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.operations.DeleteTransitionOperationDto;
import ru.urfu.lrviz.core.lr.operations.DeleteTransitionOperation;

/**
 *
 * @author fenya
 * @since 17.03.2026
 */
@Component
public class DeleteTransitionOperationConverter implements Converter<DeleteTransitionOperation, DeleteTransitionOperationDto> {
    @Override
    public DeleteTransitionOperationDto convert(DeleteTransitionOperation source) {
        return new DeleteTransitionOperationDto(source.message, source.from, source.to, source.through.asString());
    }
}
