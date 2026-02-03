package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.LRAutomatonDto;
import ru.urfu.lrviz.api.dto.LRStateDto;
import ru.urfu.lrviz.api.dto.TransitionKeyDto;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class LRAutomatonConverter implements Converter<LRAutomaton, LRAutomatonDto> {

    private final ConversionService conversionService;
    private final LRStateMapper lrStateMapper;

    public LRAutomatonConverter(ConversionService conversionService, LRStateMapper lrStateMapper) {
        this.conversionService = conversionService;
        this.lrStateMapper = lrStateMapper;
    }

    @Override
    public LRAutomatonDto convert(LRAutomaton automaton) {
        Map<String, LRState> states = automaton.namedStates();
        List<LRStateDto> convertedStates = new ArrayList<>(states.size());
        for (Map.Entry<String, LRState> entry : states.entrySet()) {
            LRStateDto convertedState = lrStateMapper.map(entry.getKey(), entry.getValue());
            convertedStates.add(convertedState);
        }

        Map<LRAutomaton.TransitionKey, String> transitions = automaton.transitionMap();
        Map<TransitionKeyDto, String> convertedTransitions = new HashMap<>(transitions.size());
        for (Map.Entry<LRAutomaton.TransitionKey, String> transition : transitions.entrySet()) {
            LRAutomaton.TransitionKey transitionKey = transition.getKey();
            TransitionKeyDto convertedKey = conversionService.convert(transitionKey, TransitionKeyDto.class);
            convertedTransitions.put(convertedKey, transition.getValue());
        }

        return new LRAutomatonDto(convertedStates, convertedTransitions);
    }
}
