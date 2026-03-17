package ru.urfu.lrviz.api.dto.operations;

/**
 * @author fenya
 * @since 17.03.2026
 */
public abstract class StateModificationOperationDto extends BuildOperationDto {

    private final String stateName;

    protected StateModificationOperationDto(String message, String operationName, String stateName) {
        super(BuildOperationDto.ACTION_LEVEL, message, operationName);
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
