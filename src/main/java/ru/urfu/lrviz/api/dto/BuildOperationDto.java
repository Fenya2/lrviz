package ru.urfu.lrviz.api.dto;

import jakarta.annotation.Nullable;


/**
 * @author fenya
 * @since 03.02.2026
 */
public record BuildOperationDto(
        String level,
        String message,
        @Nullable OperationDetails details) {

    public static final String COMMENT_LEVEL = "comment";
    public static final String ACTION_LEVEL = "action";
}
