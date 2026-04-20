package ru.urfu.lrviz.render;

/**
 * Генератор цветов для переходов автомата при его визуализации
 *
 * @author fenya
 * @since 21.04.2026
 */
public interface ColorGenerator {
    /**
     * @return очередной цвет в формате RRGGBB
     */
    String next();
}
