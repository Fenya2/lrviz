package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.operations.BuildLRAutomatonOperationDto;
import ru.urfu.lrviz.core.lr.operations.BuildLRAutomatonOperation;

/**
 * @author fenya
 * @since 15.03.2026
 */
@Component
public class BuildLRAutomatonOperationConverter implements Converter<BuildLRAutomatonOperation, BuildLRAutomatonOperationDto> {

    private final ConversionService conversionService;

    public BuildLRAutomatonOperationConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BuildLRAutomatonOperationDto convert(BuildLRAutomatonOperation source) {
        return new BuildLRAutomatonOperationDto(
                "Построим LR-автомат " + conversionService.convert(source.getAutomatonType(), String.class));
    }
}
