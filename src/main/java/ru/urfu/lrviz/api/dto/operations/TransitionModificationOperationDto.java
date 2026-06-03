package ru.urfu.lrviz.api.dto.operations;

/**
 *
 * @author fenya
 * @since 17.03.2026
 */
public abstract class TransitionModificationOperationDto extends BuildOperationDto {
    private final String from;
    private final String to;
    private final String through;

    protected TransitionModificationOperationDto(String message, String operationName, String from, String to, String through) {
        super(BuildOperationDto.ACTION_LEVEL, message, operationName);
        this.from = from;
        this.to = to;
        this.through = through;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getThrough() {
        return through;
    }
}
