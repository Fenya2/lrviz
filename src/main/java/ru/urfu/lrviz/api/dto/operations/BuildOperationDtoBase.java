package ru.urfu.lrviz.api.dto.operations;

/**
 * @author fenya
 * @since 03.02.2026
 */
public abstract class BuildOperationDtoBase {

    public static final String COMMENT_LEVEL = "comment";
    public static final String ACTION_LEVEL = "action";

    private final String level;
    private final String message;
    private final String name;

    public BuildOperationDtoBase(
            String level,
            String message, String name) {
        this.level = level;
        this.message = message;
        this.name = name;
    }

    public String level() {
        return level;
    }

    public String message() {
        return message;
    }
}
