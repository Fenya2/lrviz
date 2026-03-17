package ru.urfu.lrviz.api.dto.operations;

/**
 *
 * @author fenya
 * @since 17.03.2026
 */
public class DeleteStateOperationDto extends StateModificationOperationDto {
    public DeleteStateOperationDto(String message, String stateName) {
        super(message, "deleteState", stateName);
    }
}
