package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.api.dto.map.RuleDtoMapper;
import ru.urfu.lrviz.core.grammar.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fenya
 * @since 02.02.2026
 */
@Component
public class GrammarDtoConverter implements Converter<GrammarDto, Grammar> {

    private final RuleDtoMapper ruleDtoMapper;

    public GrammarDtoConverter(RuleDtoMapper ruleDtoMapper) {
        this.ruleDtoMapper = ruleDtoMapper;
    }

    @Override
    public Grammar convert(GrammarDto dto) {
        Set<Terminal> terminals = dto.terminals().stream().map(Terminal::new).collect(Collectors.toSet());
        Set<NonTerminal> nonTerminals = dto.nonTerminals().stream().map(NonTerminal::new).collect(Collectors.toSet());

        Map<String, GrammarSymbol> alphabet = Stream.concat(terminals.stream(), nonTerminals.stream()).collect(Collectors.toMap(
                grammarSymbol -> grammarSymbol.lexicalValue,
                Function.identity()));

        Set<Rule> rules = new HashSet<>();
        for (RuleDto ruleDto : dto.rules()) {
            rules.add(ruleDtoMapper.map(ruleDto, alphabet));
        }
        return new Grammar(terminals, nonTerminals, rules, new NonTerminal(dto.startSymbol()));
    }
}
