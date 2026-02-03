package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.TransitionKeyDto;
import ru.urfu.lrviz.core.lr.LRAutomaton;

/**
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class TransitionKeyConverter implements Converter<LRAutomaton.TransitionKey, TransitionKeyDto> {

    @Override
    public TransitionKeyDto convert(LRAutomaton.TransitionKey source) {
        return new TransitionKeyDto(source.stateName(), source.symbol().lexicalValue);
    }
}
