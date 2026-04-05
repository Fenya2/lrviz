package ru.urfu.lrviz.api;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.urfu.lrviz.api.dto.LRBuildRequestDto;
import ru.urfu.lrviz.api.dto.LrBuildResultDto;
import ru.urfu.lrviz.api.dto.map.LrBuildResultMapper;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.*;
import ru.urfu.lrviz.render.LRAutomatonVisualizer;
import ru.urfu.lrviz.render.VisualizeOptions;

import java.util.Objects;

import static org.springframework.http.MediaType.*;
import static ru.urfu.lrviz.api.VersionsConstants.FROM_V1;
import static ru.urfu.lrviz.api.openapi.OpenApiConfig.DETAILED_API_DOCS_PATH;
import static ru.urfu.lrviz.core.lr.AutomatonType.*;

/**
 * @author fenya
 * @since 02.02.2026
 */
@RestController
@Tag(name = "Построение LR-автоматов")
@RequestMapping("/build")
public class AutomatonBuildController {
    public static final String DEFAULT_IMAGE_SIZE = "1024";
    private static final String BUILD_LOG_FILENAME = "buildLog.zip";

    private final ConversionService conversionService;
    private final LRAutomatonBuilders builders;
    private final LrBuildResultMapper buildResultMapper;
    private final BuildContextCreator contextCreator;
    private final LRAutomatonVisualizer visualizer;

    public AutomatonBuildController(
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
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLr0BuildContext(targetGrammar, buildOptions);
        LRAutomaton automaton = builders.build(targetGrammar, LR_0, context);
        return buildResultMapper.map(automaton, context, buildOptions);
    }

    @PostMapping(value = "/lr0", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLR0(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLr0BuildContext(targetGrammar, buildOptions);
        LRAutomaton automaton = builders.build(targetGrammar, LR_0, context);
        VisualizeOptions visualizeOptions = Objects.requireNonNullElse(
                conversionService.convert(buildRequest.visualizeOptions(), VisualizeOptions.class), VisualizeOptions.createDefault());
        StreamingResponseBody stream = os -> visualizer.visualize(automaton, os, visualizeOptions);
        return ResponseEntity.ok().contentType(IMAGE_PNG).body(stream);
    }

    @PostMapping(value = "/lr0", version = FROM_V1, produces = APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> visualizeLr0Build(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLr0BuildContext(targetGrammar, buildOptions);
        builders.build(targetGrammar, LR_0, context);
        VisualizeOptions visualizeOptions = Objects.requireNonNullElse(
                conversionService.convert(buildRequest.visualizeOptions(), VisualizeOptions.class), VisualizeOptions.createDefault());
        StreamingResponseBody stream = os -> visualizer.visualizeBuildLog(context.getBuildLog(), os, visualizeOptions);
        return ResponseEntity.ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(BUILD_LOG_FILENAME).build().toString())
                .body(stream);
    }

    @PostMapping(value = "/lr1", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(1)-автомат", externalDocs = @ExternalDocumentation(
            description = "Подробнее", url = DETAILED_API_DOCS_PATH + "#lr1"))
    public LrBuildResultDto buildLR1(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLr1BuildContext(targetGrammar, buildOptions);
        LRAutomaton automaton = builders.build(targetGrammar, LR_1, context);
        return buildResultMapper.map(automaton, context, buildOptions);
    }

    @PostMapping(value = "/lr1", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLR1(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLr1BuildContext(targetGrammar, buildOptions);
        LRAutomaton automaton = builders.build(targetGrammar, LR_1, context);
        VisualizeOptions visualizeOptions = Objects.requireNonNullElse(
                conversionService.convert(buildRequest.visualizeOptions(), VisualizeOptions.class), VisualizeOptions.createDefault());
        StreamingResponseBody stream = os -> visualizer.visualize(automaton, os, visualizeOptions);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(stream);
    }

    @PostMapping(value = "/lr1", version = FROM_V1, produces = APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> visualizeLr1Build(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLr1BuildContext(targetGrammar, buildOptions);
        builders.build(targetGrammar, LR_1, context);
        VisualizeOptions visualizeOptions = Objects.requireNonNullElse(
                conversionService.convert(buildRequest.visualizeOptions(), VisualizeOptions.class), VisualizeOptions.createDefault());
        StreamingResponseBody stream = os -> visualizer.visualizeBuildLog(context.getBuildLog(), os, visualizeOptions);
        return ResponseEntity.ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(BUILD_LOG_FILENAME).build().toString())
                .body(stream);
    }

    @PostMapping(value = "/lalr1", version = FROM_V1, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(1)-автомат", externalDocs = @ExternalDocumentation(
            description = "Подробнее", url = DETAILED_API_DOCS_PATH + "#lalr1"))
    public LrBuildResultDto buildLALR1(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLalr1BuildContext(targetGrammar, buildOptions);
        LRAutomaton automaton = builders.build(targetGrammar, LALR, context);
        return buildResultMapper.map(automaton, context, buildOptions);
    }

    @PostMapping(value = "/lalr1", version = FROM_V1, produces = IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> renderLALR1(
            @RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLalr1BuildContext(targetGrammar, buildOptions);
        LRAutomaton automaton = builders.build(targetGrammar, LALR, context);
        StreamingResponseBody stream = os -> visualizer.visualize(
                automaton, os, VisualizeOptions.builder().build());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(stream);
    }

    @PostMapping(value = "/lalr1", version = FROM_V1, produces = APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> visualizeLalr1Build(@RequestBody LRBuildRequestDto buildRequest) {
        Grammar targetGrammar = conversionService.convert(buildRequest.grammar(), Grammar.class);
        BuildOptions buildOptions = extractBuildOptions(buildRequest);
        BuildContext context = createLalr1BuildContext(targetGrammar, buildOptions);
        builders.build(targetGrammar, LALR, context);
        VisualizeOptions visualizeOptions = Objects.requireNonNullElse(
                conversionService.convert(buildRequest.visualizeOptions(), VisualizeOptions.class), VisualizeOptions.createDefault());
        StreamingResponseBody stream = os -> visualizer.visualizeBuildLog(context.getBuildLog(), os, visualizeOptions);
        return ResponseEntity.ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(BUILD_LOG_FILENAME).build().toString())
                .body(stream);
    }

    private BuildContext createLr0BuildContext(Grammar targetGrammar, @Nullable BuildOptions buildOptions) {
        return contextCreator.createContext(LR_0, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.builder().build()));
    }

    private BuildContext createLr1BuildContext(Grammar targetGrammar, @Nullable BuildOptions buildOptions) {
        return contextCreator.createContext(
                LR_1, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.builder().build()));
    }

    private BuildContext createLalr1BuildContext(Grammar targetGrammar, @Nullable BuildOptions buildOptions) {
        return contextCreator.createContext(
                LALR, targetGrammar, Objects.requireNonNullElse(buildOptions, BuildOptions.builder().build()));
    }

    private BuildOptions extractBuildOptions(LRBuildRequestDto buildRequest) {
        return Objects.requireNonNullElse(conversionService.convert(buildRequest.buildOptions(), BuildOptions.class), BuildOptions.createEmpty());
    }
}
