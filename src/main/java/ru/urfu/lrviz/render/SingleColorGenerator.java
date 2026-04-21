package ru.urfu.lrviz.render;

/**
 *
 * @author fenya
 * @since 21.04.2026
 */
public abstract class SingleColorGenerator implements ColorGenerator {
    @Override
    public String next() {
        return getColor();
    }

    protected abstract String getColor();
}
