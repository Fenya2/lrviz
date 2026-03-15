package ru.urfu.lrviz.api.dto.operations;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public final class StartAddNewTransitionsOperationDto extends BuildOperationDto {
    public StartAddNewTransitionsOperationDto(String message) {
        super(BuildOperationDto.COMMENT_LEVEL, message, "startAddNewTransitions");
    }
}
