package ru.urfu.lrviz.api.dto.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.urfu.lrviz.api.dto.LRItemDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.grammar.Terminal;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.TransitionSymbol;
import ru.urfu.lrviz.core.lr.lalr1.LALR1Item;
import ru.urfu.lrviz.core.lr.lr0.LR0Item;
import ru.urfu.lrviz.core.lr.lr1.EndOfChainSymbol;
import ru.urfu.lrviz.core.lr.lr1.LR1Item;
import ru.urfu.lrviz.core.lr.lr1.LookAheadSymbol;

import static ru.urfu.lrviz.api.DomainConstants.END_OF_CHAIN_STRING_REPRESENTATION;

/**
 * Конвертер LR-пунктов
 *
 * @author fenya
 * @since 03.02.2026
 */
@Component
public class LRItemConverter implements Converter<LRItem, LRItemDto> {

    private final ConversionService conversionService;

    public LRItemConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public LRItemDto convert(LRItem item) {
        RuleDto convertedRule = conversionService.convert(item.getRule(), RuleDto.class);
        return switch (item) {
            case LR0Item _ -> new LRItemDto(convertedRule, item.getDotIndex(), null, null);
            case LR1Item lr1Item -> new LRItemDto(convertedRule, item.getDotIndex(), getLookAhead(lr1Item), null);
            case LALR1Item lalr1Item -> new LRItemDto(convertedRule, item.getDotIndex(), null,
                    lalr1Item.getLookAheadSymbols().stream().map(TransitionSymbol::asString).toList());
            default -> throw new IllegalArgumentException("Unable convert LR-item " + item.asString());
        };
    }

    private static String getLookAhead(LR1Item lr1Item) {
        LookAheadSymbol lookAheadSymbol = lr1Item.getLookAheadSymbol();
        return switch (lookAheadSymbol) {
            case EndOfChainSymbol _ -> END_OF_CHAIN_STRING_REPRESENTATION;
            case Terminal terminal -> terminal.lexicalValue;
            default -> throw new IllegalArgumentException("Can't convert lookahead " + lookAheadSymbol.asString());
        };
    }
}
