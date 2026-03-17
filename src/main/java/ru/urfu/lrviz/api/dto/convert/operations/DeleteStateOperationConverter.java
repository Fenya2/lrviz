package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.operations.DeleteStateOperationDto;
import ru.urfu.lrviz.core.lr.operations.DeleteStateOperation;

/**
 * @author fenya
 * @since 17.03.2026
 */
@Component
public class DeleteStateOperationConverter implements Converter<DeleteStateOperation, DeleteStateOperationDto> {
    @Override
    public DeleteStateOperationDto convert(DeleteStateOperation operation) {
        return new DeleteStateOperationDto(operation.message, operation.stateName);
    }
}
