package ru.urfu.lrviz.api.dto.map;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.grammar.NonTerminal;
import ru.urfu.lrviz.core.grammar.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author fenya
 * @since 28.02.2026
 */
@Service
public class RuleDtoMapper {
    public Rule map(RuleDto ruleDto, Map<String, GrammarSymbol> alphabet) {
        NonTerminal left = (NonTerminal) Objects.requireNonNull(alphabet.get(ruleDto.left()));
        String rightPart = Objects.requireNonNull(ruleDto.right());
        if (rightPart.isEmpty()) {
            return Rule.ofEmpty(left);
        }
        List<GrammarSymbol> right = new ArrayList<>(rightPart.length());
        for (String symbol : rightPart.split("")) {
            right.add(Objects.requireNonNull(alphabet.get(symbol)));
        }
        return new Rule(left, right);
    }
}
