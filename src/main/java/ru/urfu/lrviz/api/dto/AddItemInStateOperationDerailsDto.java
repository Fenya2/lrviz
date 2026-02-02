package ru.urfu.lrviz.api.dto;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public record AddItemInStateOperationDerailsDto(String stateName, LRItemDto item) implements OperationDetails {
    public static final String NAME = "addItem";
}
