package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.grammar.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GrammarConverter implements Converter<GrammarDto, Grammar> {

    @Override
    public Grammar convert(GrammarDto dto) {
        Set<Terminal> terminals = dto.terminals().stream().map(Terminal::new).collect(Collectors.toSet());
        Set<NonTerminal> nonTerminals = dto.nonTerminals().stream().map(NonTerminal::new).collect(Collectors.toSet());

        Map<String, GrammarSymbol> alphabet = Stream.concat(terminals.stream(), nonTerminals.stream()).collect(Collectors.toMap(
                grammarSymbol -> grammarSymbol.lexicalValue,
                Function.identity()));

        Set<Rule> rules = new HashSet<>();
        for (RuleDto ruleDto : dto.rules()) {
            NonTerminal left = (NonTerminal) alphabet.get(ruleDto.left());

            String rightPart = ruleDto.right();
            if (rightPart.isEmpty()) {
                rules.add(new Rule(left, Collections.emptyList()));
                continue;
            }
            List<GrammarSymbol> right = new ArrayList<>();
            for (String symbol : rightPart.split("")) {
                right.add(alphabet.get(symbol));
            }
            rules.add(new Rule(left, right));
        }
        return new Grammar(terminals, nonTerminals, rules, new NonTerminal(dto.startSymbol()));
    }
}
