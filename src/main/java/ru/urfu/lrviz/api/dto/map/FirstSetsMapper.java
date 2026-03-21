package ru.urfu.lrviz.api.dto.map;

import org.springframework.stereotype.Component;
import ru.urfu.lrviz.core.grammar.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.urfu.lrviz.api.DomainConstants.EPSILON_STRING_REPRESENTATION;

/**
 * @author fenya
 * @since 21.03.2026
 */
@Component
public class FirstSetsMapper {
    public Map<String, List<String>> map(Map<GrammarSymbol, Set<FirstSetMember>> firstSets) {
        return firstSets.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof NonTerminal)
                .collect(Collectors.toMap(
                entry -> entry.getKey().lexicalValue,
                entry -> entry.getValue().stream().map(firstSetMember -> switch (firstSetMember) {
                    case Epsilon _ -> EPSILON_STRING_REPRESENTATION;
                    case Terminal terminal -> terminal.lexicalValue;
                    default -> throw new IllegalStateException("Unexpected value: " + firstSetMember);
                }).toList()
        ));
    }
}
