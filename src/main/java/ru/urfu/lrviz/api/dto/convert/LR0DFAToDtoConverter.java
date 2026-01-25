package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.LR0DFADto;
import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.LR0Item;

@Component
public class LR0DFAToDtoConverter implements Converter<DFA<LR0Item, GrammarSymbol>, LR0DFADto> {
    @Override
    public LR0DFADto convert(DFA<LR0Item, GrammarSymbol> source) {
        return null;
    }
}
