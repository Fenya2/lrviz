package ru.urfu.lrviz.api.dto;

/**
 * Детали операции построения автомата
 *
 * @author fenya
 * @since 03.02.2026
 */
public record AddStateOperationDetailsDto(String stateName) implements OperationDetails {
    public static final String NAME = "addState";
}
