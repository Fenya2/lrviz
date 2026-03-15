package ru.urfu.lrviz.api.dto.operations;

/**
 *
 * @author fenya
 * @since 15.03.2026
 */
public class CompactLRAutomatonOperationDto extends BuildOperationDto {
    public CompactLRAutomatonOperationDto(String message) {
        super(COMMENT_LEVEL, message, "compactLRAutomaton");
    }
}
