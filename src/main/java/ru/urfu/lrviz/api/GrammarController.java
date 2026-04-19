package ru.urfu.lrviz.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.map.FirstSetsMapper;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarService;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.urfu.lrviz.api.VersionsConstants.FROM_V1;
import static ru.urfu.lrviz.api.docs.OpenApiConfig.DETAILED_API_DOCS_PATH;

/**
 * @author fenya
 * @since 21.03.2026
 */
@RestController
@Tag(name = "Работа с грамматиками")
@RequestMapping("/grammar")
public class GrammarController {

    private final ConversionService conversionService;
    private final GrammarService grammarService;
    private final FirstSetsMapper firstSetMapper;

    public GrammarController(ConversionService conversionService, GrammarService grammarService, FirstSetsMapper firstSetMapper) {
        this.conversionService = conversionService;
        this.grammarService = grammarService;
        this.firstSetMapper = firstSetMapper;
    }

    @PostMapping(value = "/first", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит множества FIRST", externalDocs = @ExternalDocumentation(description = "Подробнее",
            url = DETAILED_API_DOCS_PATH + "#first"))
    public Map<String, List<String>> buildFirst(@RequestBody GrammarDto grammarDto) {
        Grammar grammar = conversionService.convert(grammarDto, Grammar.class);
        return firstSetMapper.map(grammarService.getFirst(grammar));
    }
}