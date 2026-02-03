package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.BuildLogDto;
import ru.urfu.lrviz.api.dto.operations.BuildOperationDto;
import ru.urfu.lrviz.core.lr.BuildLog;
import ru.urfu.lrviz.core.lr.operations.BuildOperation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
@Component
public class BuildLogConverter implements Converter<BuildLog, BuildLogDto> {

    private final ConversionService conversionService;

    public BuildLogConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BuildLogDto convert(BuildLog log) {
        List<BuildOperation> operations = log.getOperations();
        List<BuildOperationDto> convertedOperations = new ArrayList<>(operations.size());
        for (BuildOperation operation : operations) {
            BuildOperationDto convertedOperation = conversionService.convert(operation, BuildOperationDto.class);
            convertedOperations.add(convertedOperation);
        }
        return new BuildLogDto(convertedOperations);
    }
}
