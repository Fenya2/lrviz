package ru.urfu.lrviz.api.dto.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.LRDFADto;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.LRStateDto;
import ru.urfu.lrviz.api.dto.TransitionKeyDto;
import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.LR0AutomatonState;
import ru.urfu.lrviz.core.lr.LR0Item;
import ru.urfu.lrviz.core.lr.LRAutomatonState;

import java.util.*;

@Component
public class AutomatonConverter {

    private final ConversionService conversionService;

    @Autowired
    public AutomatonConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public LRDFADto toDto(DFA<? extends LRAutomatonState, GrammarSymbol> automaton) {
        if (automaton.getStates().isEmpty()) {
            return new LRDFADto(Collections.emptyList(), Collections.emptyMap());
        }
        if (automaton.getStates().iterator().next() instanceof LR0AutomatonState) {
            return mapLR0(automaton);
        }
        throw new IllegalArgumentException("Automaton type is not supported.");
    }

    private LRDFADto mapLR0(DFA<? extends LRAutomatonState, GrammarSymbol> automaton) {
        List<LRStateDto> states = new ArrayList<>(automaton.getStates().size());
        for (LRAutomatonState state : automaton.getStates()) {
            states.add(mapLR0State((LR0AutomatonState) state));
        }

        Map<TransitionKeyDto, String> transitions = HashMap.newHashMap(automaton.getTransitionMap().size());
        automaton.getTransitionMap().forEach(
                (key, value) -> transitions.put(
                        new TransitionKeyDto(key.state().getName(), key.symbol().lexicalValue),
                        value.getName()));
        return new LRDFADto(states, transitions);
    }

    private LRStateDto mapLR0State(LR0AutomatonState state) {
        List<LRItemDto> items = new ArrayList<>(state.getItems().size());
        for (LR0Item item : state.getItems()) {
            items.add(conversionService.convert(item, LRItemDto.class));
        }
        return new LRStateDto(state.getName(), items);
    }
}
