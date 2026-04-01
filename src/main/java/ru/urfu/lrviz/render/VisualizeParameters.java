package ru.urfu.lrviz.render;

/**
 * @author fenya
 * @since 28.02.2026
 */
public final class VisualizeParameters {
    private static final int MAX_SIZE = 10000;

    private final int size;
    private final RenderFormat format;
    private final boolean highLightBaseItems;

    public static class VisualizeParametersBuilder {
        private int size = 1024;
        private RenderFormat renderFormat = RenderFormat.PNG;
        private boolean highLightBaseItems = false;

        private VisualizeParametersBuilder() {
        }

        /**
         * Размер изображения
         */
        public VisualizeParametersBuilder size(int size) {
            if (size <= 0 || size > MAX_SIZE) {
                throw new IllegalArgumentException();
            }
            this.size = size;
            return this;
        }

        public VisualizeParametersBuilder renderFormat(RenderFormat renderFormat) {
            this.renderFormat = renderFormat;
            return this;
        }

        public VisualizeParametersBuilder highLightBaseItems(boolean highLightBaseItems) {
            this.highLightBaseItems = highLightBaseItems;
            return this;
        }

        public VisualizeParameters build() {
            return new VisualizeParameters(size, renderFormat, highLightBaseItems);
        }
    }

    /**
     * @param size               размер изображения
     * @param format             формат изображения
     * @param highLightBaseItems подсвечивать ли базовые пункты автомата
     *
     */
    private VisualizeParameters(int size, RenderFormat format, boolean highLightBaseItems) {
        this.size = size;
        this.format = format;
        this.highLightBaseItems = highLightBaseItems;
    }

    public static VisualizeParametersBuilder builder() {
        return new VisualizeParametersBuilder();
    }

    public int size() {
        return size;
    }

    public RenderFormat format() {
        return format;
    }

    public boolean highLightBaseItems() {
        return highLightBaseItems;
    }
}
