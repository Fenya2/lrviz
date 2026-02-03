package ru.urfu.lrviz.api.dto.operations;

import ru.urfu.lrviz.api.dto.LRItemDto;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public final class AddItemInStateOperationDto extends BuildOperationDto {
    private final String state;
    private final LRItemDto item;

    public AddItemInStateOperationDto(String message, String state, LRItemDto item) {
        super(BuildOperationDto.ACTION_LEVEL, message, "addItem");
        this.item = item;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public LRItemDto getItem() {
        return item;
    }
}
