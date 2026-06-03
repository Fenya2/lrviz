package ru.urfu.lrviz.api.dto.operations;

/**
 *
 * @author fenya
 * @since 15.03.2026
 */
public final class BuildLRAutomatonOperationDto extends BuildOperationDto {
    public BuildLRAutomatonOperationDto(String message) {
        super(BuildOperationDto.COMMENT_LEVEL, message, "buildLrAutomaton");
    }
}
