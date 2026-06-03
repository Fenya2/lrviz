package ru.urfu.lrviz.api.dto.map;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.TransitionDto;
import ru.urfu.lrviz.core.lr.TransitionKey;

/**
 * @author fenya
 * @since 03.02.2026
 */
@Component
public class TransitionMapper {
    public TransitionDto map(TransitionKey from, String to) {
        return new TransitionDto(from.stateName(), to, from.symbol().asString());
    }
}
