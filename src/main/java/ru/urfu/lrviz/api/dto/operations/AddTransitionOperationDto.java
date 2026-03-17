package ru.urfu.lrviz.api.dto.operations;

/**
 * @author fenya
 * @since 03.02.2026
 */
public final class AddTransitionOperationDto extends TransitionModificationOperationDto {
    public AddTransitionOperationDto(String message, String from, String to, String through) {
        super(message, "addTransition", from, to, through);
    }
}
