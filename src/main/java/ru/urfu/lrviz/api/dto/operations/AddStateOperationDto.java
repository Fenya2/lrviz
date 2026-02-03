package ru.urfu.lrviz.api.dto.operations;

/**
 * Детали операции построения автомата
 *
 * @author fenya
 * @since 03.02.2026
 */
public final class AddStateOperationDto extends BuildOperationDto {
    private final String stateName;

    public AddStateOperationDto(String message, String stateName) {
        super(BuildOperationDto.ACTION_LEVEL, message, "addState");
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
