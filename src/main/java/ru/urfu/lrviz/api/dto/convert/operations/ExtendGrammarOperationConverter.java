package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.operations.ExtendGrammarOperationDto;
import ru.urfu.lrviz.core.lr.operations.ExtendGrammarOperation;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class ExtendGrammarOperationConverter implements Converter<ExtendGrammarOperation, ExtendGrammarOperationDto> {
    @Override
    public ExtendGrammarOperationDto convert(ExtendGrammarOperation operation) {
        return new ExtendGrammarOperationDto(operation.message);
    }
}
