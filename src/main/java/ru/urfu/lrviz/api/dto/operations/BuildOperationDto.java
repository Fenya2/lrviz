package ru.urfu.lrviz.api.dto.operations;

/**
 * @author fenya
 * @since 03.02.2026
 */
public abstract class BuildOperationDto {

    public static final String COMMENT_LEVEL = "comment";
    public static final String ACTION_LEVEL = "action";

    private final String level;
    private final String message;
    private final String name;

    protected BuildOperationDto(
            String level,
            String message, String name) {
        this.level = level;
        this.message = message;
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BuildOperationDto{" +
                "level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
