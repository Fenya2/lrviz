package ru.urfu.lrviz.render;

/**
 * Возвращает всегда черный цвет
 *
 * @author fenya
 * @since 21.04.2026
 */
public class BlackColorGenerator extends SingleColorGenerator {
    private static final BlackColorGenerator INSTANCE = new BlackColorGenerator();

    public static BlackColorGenerator create() {
        return INSTANCE;
    }

    private BlackColorGenerator() {

    }

    @Override
    protected String getColor() {
        return "000000";
    }
}
