package ru.urfu.lrviz.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.urfu.lrviz.api.dto.LRBuildRequestDto;
import ru.urfu.lrviz.api.dto.LrBuildResultDto;
import ru.urfu.lrviz.api.dto.map.LrBuildResultMapper;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.*;
import ru.urfu.lrviz.render.LRAutomatonVisualizer;
import ru.urfu.lrviz.render.VisualizeParameters;

import java.util.Objects;

import static org.springframework.http.MediaType.*;
import static ru.urfu.lrviz.api.VersionsConstants.FROM_V1;
import static ru.urfu.lrviz.api.openapi.OpenApiConfig.DETAILED_API_DOCS_PATH;
import static ru.urfu.lrviz.core.lr.AutomatonType.*;
import static ru.urfu.lrviz.render.RenderFormat.PNG;

/**
 * @author fenya
 * @since 02.02.2026
 */
@RestController
@Tag(name = "Построение LR-автоматов")
@RequestMapping("/build")
public class AutomatonController {
    public static final String DEFAULT_IMAGE_SIZE = "1024";

    private final ConversionService conversionService;
    private final LRAutomatonBuilders builders;
    private final LrBuildResultMapper buildResultMapper;
    private final BuildContextCreator contextCreator;
    private final LRAutomatonVisualizer visualizer;

    public AutomatonController(
            ConversionService conversionService,
            LRAutomatonBuilders builders,
            LrBuildResultMapper buildResultMapper,
            BuildContextCreator contextCreator, LRAutomatonVisualizer visualizer) {
        this.conversionService = conversionService;
        this.builders = builders;
        this.buildResultMapper = buildResultMapper;
        this.contextCreator = contextCreator;
        this.visualizer = visualizer;
    }

    @PostMapping(value = "/lr0", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(0)-автомат", externalDocs = @ExternalDocumentation(description = "Подробнее",
            url = DETAILED_API_DOCS_PATH + "#lr0"))
    public LrBuildResultDto buildLR0(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = conversionService.convert(buildRequest.buildOptions(), BuildOptions.class);
        BuildContext context = contextCreator.createContext(
                AutomatonType.LR_0, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.createEmpty()));
        LRAutomaton automaton = builders.build(targetGrammar, LR_0, context);
        return buildResultMapper.map(automaton, context);
    }

    @PostMapping(value = "/lr0", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLR0(
            @RequestParam(defaultValue = DEFAULT_IMAGE_SIZE) int size,
            @RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = conversionService.convert(buildRequest.buildOptions(), BuildOptions.class);
        BuildContext context = contextCreator.createContext(
                LR_0, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.createEmpty()));
        LRAutomaton automaton = builders.build(targetGrammar, LR_0, context);
        StreamingResponseBody stream = os -> visualizer.visualize(automaton, os, new VisualizeParameters(size, PNG, false));
        return ResponseEntity.ok().contentType(IMAGE_PNG).body(stream);
    }

    @PostMapping(value = "/lr1", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(1)-автомат", externalDocs = @ExternalDocumentation(
            description = "Подробнее", url = DETAILED_API_DOCS_PATH + "#lr1"))
    public LrBuildResultDto buildLR1(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = conversionService.convert(buildRequest.buildOptions(), BuildOptions.class);
        BuildContext context = contextCreator.createContext(
                LR_1, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.createEmpty()));
        LRAutomaton automaton = builders.build(targetGrammar, LR_1, context);
        return buildResultMapper.map(automaton, context);
    }

    @PostMapping(value = "/lr1", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLR1(
            @RequestParam(defaultValue = DEFAULT_IMAGE_SIZE) int size,
            @RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = conversionService.convert(buildRequest.buildOptions(), BuildOptions.class);
        BuildContext context = contextCreator.createContext(
                LR_1, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.createEmpty()));
        LRAutomaton automaton = builders.build(targetGrammar, LR_1, context);
        StreamingResponseBody stream = os -> visualizer.visualize(automaton, os, new VisualizeParameters(size, PNG, false));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(stream);
    }

    @PostMapping(value = "/lalr1", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(1)-автомат", externalDocs = @ExternalDocumentation(
            description = "Подробнее", url = DETAILED_API_DOCS_PATH + "#lalr1"))
    public LrBuildResultDto buildLALR1(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = conversionService.convert(buildRequest.buildOptions(), BuildOptions.class);
        BuildContext context = contextCreator.createContext(
                LALR, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.createEmpty()));
        LRAutomaton automaton = builders.build(targetGrammar, LALR, context);
        return buildResultMapper.map(automaton, context);
    }

    @PostMapping(value = "/lalr1", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLALR1(
            @RequestParam(defaultValue = DEFAULT_IMAGE_SIZE) int size,
            @RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = conversionService.convert(buildRequest.buildOptions(), BuildOptions.class);
        BuildContext context = contextCreator.createContext(
                LALR, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.createEmpty()));
        LRAutomaton automaton = builders.build(targetGrammar, LALR, context);
        StreamingResponseBody stream = os -> visualizer.visualize(automaton, os, new VisualizeParameters(size, PNG, true));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(stream);
    }
}
