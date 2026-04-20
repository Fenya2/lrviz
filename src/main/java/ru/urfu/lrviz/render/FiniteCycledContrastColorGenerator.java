package ru.urfu.lrviz.render;

/**
 * Перебирает по кругу 20 контрастных цветов
 *
 * @author fenya
 * @since 21.04.2026
 */
public class FiniteCycledContrastColorGenerator implements ColorGenerator {

    private int index = 0;

    private final String[] palette = {
            "FF0000", "00FF00", "0000FF", "FFA500", "800080",
            "FFC0CB", "008080", "FF00FF", "4B0082", "00FFFF",
            "7F0000", "007F00", "00007F", "7F7F00", "7F007F",
            "007F7F", "8B4513", "2E8B57", "9400D3", "FF1493"};

    @Override
    public String next() {
        String color = palette[index % palette.length];
        index++;
        return color;
    }
}
