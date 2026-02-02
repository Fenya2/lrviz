package ru.urfu.lrviz.api.dto;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public record AddTransitionOperationDetailsDto(String fromState, String toState,
                                               String through) implements OperationDetails {
    public static final String NAME = "addTransition";
}
