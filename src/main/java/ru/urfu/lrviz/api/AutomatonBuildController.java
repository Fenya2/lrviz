package ru.urfu.lrviz.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.LrBuildResultDto;
import ru.urfu.lrviz.api.dto.map.LrBuildResultMapper;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.*;
import ru.urfu.lrviz.graphviz.LRAutomationGraphRenderer;
import ru.urfu.lrviz.graphviz.RenderParameters;

import static org.springframework.http.MediaType.*;
import static ru.urfu.lrviz.api.VersionsConstants.FROM_V1;
import static ru.urfu.lrviz.api.openapi.OpenApiConfig.DETAILED_API_DOCS_PATH;
import static ru.urfu.lrviz.graphviz.RenderFormat.PNG;

/**
 * @author fenya
 * @since 02.02.2026
 */
@RestController
@Tag(name = "Построение LR-автоматов")
@RequestMapping("/build")
public class AutomatonBuildController {
    public static final String DEFAULT_IMAGE_SIZE = "1024";

    private final ConversionService conversionService;
    private final LRAutomatonBuilders builders;
    private final LrBuildResultMapper buildResultMapper;
    private final BuildContextCreator contextCreator;
    private final LRAutomationGraphRenderer renderer;

    public AutomatonBuildController(
            ConversionService conversionService,
            LRAutomatonBuilders builders,
            LrBuildResultMapper buildResultMapper,
            BuildContextCreator contextCreator, LRAutomationGraphRenderer renderer) {
        this.conversionService = conversionService;
        this.builders = builders;
        this.buildResultMapper = buildResultMapper;
        this.contextCreator = contextCreator;
        this.renderer = renderer;
    }

    @PostMapping(value = "/lr0", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(0)-автомат", externalDocs = @ExternalDocumentation(description = "Подробнее",
            url = DETAILED_API_DOCS_PATH + "#lr0"))
    public LrBuildResultDto buildLR0(@RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR0Context();
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_0, context);
        return buildResultMapper.map(automaton, context);
    }

    @PostMapping(value = "/lr0", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLR0InPng(
            @RequestParam(defaultValue = DEFAULT_IMAGE_SIZE) int size,
            @RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR0Context();
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_0, context);
        StreamingResponseBody stream = os -> renderer.render(automaton, os, new RenderParameters(size, PNG));
        return ResponseEntity.ok().contentType(IMAGE_PNG).body(stream);
    }

    @PostMapping(value = "/lr1", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(1)-автомат", externalDocs = @ExternalDocumentation(
            description = "Подробнее", url = DETAILED_API_DOCS_PATH + "#lr1"))
    public LrBuildResultDto buildLR1(@RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR1Context(targetGrammar);
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_1, context);
        return buildResultMapper.map(automaton, context);
    }

    @PostMapping(value = "/lr1", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLR1(
            @RequestParam(defaultValue = DEFAULT_IMAGE_SIZE) int size,
            @RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR1Context(targetGrammar);
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_1, context);
        StreamingResponseBody stream = os -> renderer.render(automaton, os, new RenderParameters(size, PNG));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(stream);
    }
}
