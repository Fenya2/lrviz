package ru.urfu.lrviz.api.dto.convert;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.TransitionDto;
import ru.urfu.lrviz.core.lr.LRAutomaton;

/**
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class TransitionMapper {
    public TransitionDto map(LRAutomaton.TransitionKey from, String to) {
        return new TransitionDto(from.stateName(), to, from.symbol().lexicalValue);
    }
}
