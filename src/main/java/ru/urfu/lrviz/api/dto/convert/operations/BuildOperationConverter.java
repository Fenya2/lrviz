package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.operations.*;
import ru.urfu.lrviz.core.lr.operations.*;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class BuildOperationConverter implements Converter<BuildOperation, BuildOperationDto> {

    private final ConversionService conversionService;

    public BuildOperationConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BuildOperationDto convert(BuildOperation operation) {
        return switch (operation) {
            case AddStateOperation _ -> conversionService.convert(operation, AddStateOperationDto.class);
            case AddItemInStateOperation _ -> conversionService.convert(operation, AddItemInStateOperationDto.class);
            case AddTransitionOperation _ -> conversionService.convert(operation, AddTransitionOperationDto.class);
            case DeleteStateOperation _ -> conversionService.convert(operation, DeleteStateOperationDto.class);
            case DeleteTransitionOperation _ ->
                    conversionService.convert(operation, DeleteTransitionOperationDto.class);
            case ExtendGrammarOperation _ -> conversionService.convert(operation, ExtendGrammarOperationDto.class);
            case StartAddNewTransitionsOperation _ ->
                    conversionService.convert(operation, StartAddNewTransitionsOperationDto.class);
            case BuildLRAutomatonOperation _ ->
                    conversionService.convert(operation, BuildLRAutomatonOperationDto.class);
            case CompactLRAutomatonOperation _ ->
                    conversionService.convert(operation, CompactLRAutomatonOperationDto.class);
            case AddLookAheadOperation _ -> conversionService.convert(operation, AddLookAheadOperationDto.class);
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };
    }
}
