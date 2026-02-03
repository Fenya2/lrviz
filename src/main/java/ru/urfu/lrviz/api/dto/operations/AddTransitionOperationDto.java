package ru.urfu.lrviz.api.dto.operations;

/**
 * @author fenya
 * @since 03.02.2026
 */
public final class AddTransitionOperationDto extends BuildOperationDto {
    private final String from;
    private final String to;
    private final String through;

    public AddTransitionOperationDto(String message, String from, String to, String through) {
        super(BuildOperationDto.ACTION_LEVEL, message, "addTransition");
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
