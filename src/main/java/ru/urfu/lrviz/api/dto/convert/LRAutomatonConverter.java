package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.LRAutomatonDto;
import ru.urfu.lrviz.api.dto.LRStateDto;
import ru.urfu.lrviz.api.dto.TransitionDto;
import ru.urfu.lrviz.api.dto.map.LRStateMapper;
import ru.urfu.lrviz.api.dto.map.TransitionMapper;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fenya
 * @since 03.02.2026
 */
@Component
public class LRAutomatonConverter implements Converter<LRAutomaton, LRAutomatonDto> {

    private final LRStateMapper lrStateMapper;
    private final TransitionMapper transitionMapper;

    public LRAutomatonConverter(LRStateMapper lrStateMapper, TransitionMapper transitionMapper) {
        this.lrStateMapper = lrStateMapper;
        this.transitionMapper = transitionMapper;
    }

    @Override
    public LRAutomatonDto convert(LRAutomaton automaton) {
        Map<String, LRState> states = automaton.namedStates();
        List<LRStateDto> convertedStates = new ArrayList<>(states.size());
        for (Map.Entry<String, LRState> entry : states.entrySet()) {
            LRStateDto convertedState = lrStateMapper.map(entry.getKey(), entry.getValue());
            convertedStates.add(convertedState);
        }

        Map<LRAutomaton.TransitionKey, String> transitions = automaton.transitions();
        List<TransitionDto> convertedTransitions = new ArrayList<>(transitions.size());
        for (Map.Entry<LRAutomaton.TransitionKey, String> transition : transitions.entrySet()) {
            LRAutomaton.TransitionKey transitionKey = transition.getKey();
            TransitionDto convertedTransition = transitionMapper.map(transitionKey, transition.getValue());
            convertedTransitions.add(convertedTransition);
        }

        return new LRAutomatonDto(convertedStates, convertedTransitions);
    }
}
