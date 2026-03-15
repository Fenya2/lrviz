package ru.urfu.lrviz.api.dto.convert.operations;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.operations.CompactLRAutomatonOperationDto;
import ru.urfu.lrviz.core.lr.operations.CompactLRAutomatonOperation;

/**
 * @author fenya
 * @since 15.03.2026
 */
@Component
public class CompactLRAutomatonOperationConverter implements Converter<CompactLRAutomatonOperation, CompactLRAutomatonOperationDto> {
    @Override
    public CompactLRAutomatonOperationDto convert(CompactLRAutomatonOperation source) {
        return new CompactLRAutomatonOperationDto(source.message);
    }
}
