package ru.urfu.lrviz.api.dto.operations;

import ru.urfu.lrviz.api.dto.LRItemDto;

/**
 *
 * @author fenya
 * @since 30.03.2026
 */
public class AddLookAheadOperationDto extends BuildOperationDto {
    private final String stateName;
    private final LRItemDto item;
    private final String lookAheadSymbol;

    public AddLookAheadOperationDto(String message, String stateName, LRItemDto item, String lookAheadSymbol) {
        super(ACTION_LEVEL, message, "addLookAheadSymbol");
        this.stateName = stateName;
        this.item = item;
        this.lookAheadSymbol = lookAheadSymbol;
    }

    public String getStateName() {
        return stateName;
    }

    public LRItemDto getItem() {
        return item;
    }

    public String getLookAheadSymbol() {
        return lookAheadSymbol;
    }
}
