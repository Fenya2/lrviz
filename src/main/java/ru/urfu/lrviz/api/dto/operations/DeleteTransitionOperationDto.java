package ru.urfu.lrviz.api.dto.operations;

/**
 * @author fenya
 * @since 17.03.2026
 */
public class DeleteTransitionOperationDto extends TransitionModificationOperationDto {
    public DeleteTransitionOperationDto(String message, String from, String to, String through) {
        super(message, "deleteTransition", from, to, through);
    }
}
