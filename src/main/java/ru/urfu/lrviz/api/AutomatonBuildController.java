package ru.urfu.lrviz.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.LrBuildResultDto;
import ru.urfu.lrviz.api.dto.convert.LrBuildResultMapper;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.*;

import static ru.urfu.lrviz.api.VersionsConstants.FROM_V1;
import static ru.urfu.lrviz.api.openapi.OpenApiConfig.DETAILED_API_DOCS_PATH;

/**
 * @author fenya
 * @since 02.02.2026
 */
@RestController
@Tag(name = "Построение LR-автоматов")
@RequestMapping("/build")
public class AutomatonBuildController {
    private final ConversionService conversionService;
    private final LRAutomatonBuilders builders;
    private final LrBuildResultMapper buildResultMapper;
    private final BuildContextCreator contextCreator;

    public AutomatonBuildController(
            ConversionService conversionService,
            LRAutomatonBuilders builders,
            LrBuildResultMapper buildResultMapper,
            BuildContextCreator contextCreator) {
        this.conversionService = conversionService;
        this.builders = builders;
        this.buildResultMapper = buildResultMapper;
        this.contextCreator = contextCreator;
    }

    @PostMapping(value = "/lr0", produces = MediaType.APPLICATION_JSON_VALUE, version = FROM_V1)
    @Operation(summary = "Строит LR(0)-автомат", externalDocs = @ExternalDocumentation(
            description = "Подробнее",
            url = DETAILED_API_DOCS_PATH + "#lr0"))
    public LrBuildResultDto buildLR0(@RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR0Context();
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_0, context);
        return buildResultMapper.map(automaton, context);
    }

    @PostMapping(value = "/lr1", produces = MediaType.APPLICATION_JSON_VALUE, version = FROM_V1)
    @Operation(summary = "Строит LR(1)-автомат", externalDocs = @ExternalDocumentation(
            description = "Подробнее",
            url = DETAILED_API_DOCS_PATH + "#lr1"))
    public LrBuildResultDto buildLR1(@RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR1Context(targetGrammar);
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_1, context);
        return buildResultMapper.map(automaton, context);
    }
}
