package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import ru.urfu.lrviz.api.dto.operations.StartBuildLR1OperationDto;
import ru.urfu.lrviz.core.lr.operations.StartBuildLR1Operation;

/**
 * @author fenya
 * @since 10.03.2026
 */
public class BuildLR1OperationConverter implements Converter<StartBuildLR1Operation, StartBuildLR1OperationDto> {
    @Override
    public StartBuildLR1OperationDto convert(StartBuildLR1Operation source) {
        return new StartBuildLR1OperationDto(source.message);
    }
}
