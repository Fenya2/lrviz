package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.operations.StartAddNewTransitionsOperationDto;
import ru.urfu.lrviz.core.lr.operations.StartAddNewTransitionsOperation;

/**
 * @author fenya
 * @since 03.02.2026
 */
@Component
public class StartAddNewTransitionsOperationConverter
        implements Converter<StartAddNewTransitionsOperation, StartAddNewTransitionsOperationDto> {
    @Override
    public StartAddNewTransitionsOperationDto convert(StartAddNewTransitionsOperation source) {
        return new StartAddNewTransitionsOperationDto(source.message);
    }
}
