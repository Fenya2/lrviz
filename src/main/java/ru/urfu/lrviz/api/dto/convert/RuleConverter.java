package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.grammar.Rule;

import java.util.stream.Collectors;

/**
 *
 * @author fenya
 * @since 03.02.2026
 */
@Service
public class RuleConverter implements Converter<Rule, RuleDto> {
    @Override
    public RuleDto convert(Rule rule) {
        String right = rule.right().stream()
                .map(grammarSymbol -> grammarSymbol.lexicalValue).collect(Collectors.joining());
        return new RuleDto(rule.left().lexicalValue, right);
    }
}
