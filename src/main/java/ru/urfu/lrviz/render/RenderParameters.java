package ru.urfu.lrviz.render;

/**
 * @author fenya
 * @since 28.02.2026
 */
public record RenderParameters(int size, RenderFormat format) {

    private static final int MAX_SIZE = 10000;

    public RenderParameters {

        if (size <= 0 || size > MAX_SIZE) {
            throw new IllegalArgumentException();
        }
    }
}
