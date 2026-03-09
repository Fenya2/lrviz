package ru.urfu.lrviz.api.dto.operations;

/**
 *
 * @author fenya
 * @since 10.03.2026
 */
public class StartBuildLR1OperationDto extends BuildOperationDto {
    public StartBuildLR1OperationDto(String message) {
        super(COMMENT_LEVEL, message, "buildLr1");
    }
}
