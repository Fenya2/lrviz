package ru.urfu.lrviz.render;

/**
 * @param size размер изображения
 * @param format формат изображения
 * @param highLightBaseItems подсвечивать ли базовые пункты автомата
 * @author fenya
 * @since 28.02.2026
 */
public record RenderParameters(int size, RenderFormat format, boolean highLightBaseItems) {

    private static final int MAX_SIZE = 10000;

    public RenderParameters {

        if (size <= 0 || size > MAX_SIZE) {
            throw new IllegalArgumentException();
        }
    }
}
