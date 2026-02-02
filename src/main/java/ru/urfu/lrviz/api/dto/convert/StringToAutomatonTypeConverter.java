package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.lr.AutomatonType;

@Component
public class StringToAutomatonTypeConverter implements Converter<String, AutomatonType> {

    @Override
    public AutomatonType convert(String source) {
        if ("lr0".equals(source)) {
            return AutomatonType.LR_0;
        }
        throw new IllegalArgumentException("Unknown automaton type: " + source);
    }
}
