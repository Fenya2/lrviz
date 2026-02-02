package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import ru.urfu.lrviz.api.dto.LRAutomatonDto;
import ru.urfu.lrviz.core.lr.LRAutomaton;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
public class AutomatonConverter implements Converter<LRAutomaton, LRAutomatonDto> {

    @Override
    public LRAutomatonDto convert(LRAutomaton source) {
        // TODO
        return null;
    }
}
