package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.lr.AutomatonType;

/**
 *
 * @author fenya
 * @since 15.03.2026
 */
@Component
public class AutomatonTypeConverter implements Converter<AutomatonType, String> {
    @Override
    public String convert(AutomatonType type) {
        return switch (type) {
            case LR_0 -> "lr(0)";
            case LALR -> "lalr(1)";
            case LR_1 -> "lr(1)";
        };
    }
}