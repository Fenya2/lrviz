package ru.urfu.lrviz.api.dto.operations;

import ru.urfu.lrviz.api.dto.LRItemDto;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public final class AddItemInStateOperationDto extends BuildOperationDtoBase {
    private final LRItemDto item;

    public AddItemInStateOperationDto(String message, LRItemDto item) {
        super(BuildOperationDtoBase.ACTION_LEVEL, message, "addItem");
        this.item = item;
    }
}
