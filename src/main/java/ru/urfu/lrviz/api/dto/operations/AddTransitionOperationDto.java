package ru.urfu.lrviz.api.dto.operations;

/**
 * @author fenya
 * @since 03.02.2026
 */
public final class AddTransitionOperationDto extends BuildOperationDtoBase {
    private final String fromState;
    private final String toState;
    private final String through;

    public AddTransitionOperationDto(String message, String fromState, String toState, String through) {
        super(BuildOperationDtoBase.ACTION_LEVEL, message, "addTransition");
        this.fromState = fromState;
        this.toState = toState;
        this.through = through;
    }
}
