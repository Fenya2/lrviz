package ru.urfu.lrviz.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.urfu.lrviz.api.dto.LRAutomatonDto;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.graphviz.LRAutomationGraphRenderer;
import ru.urfu.lrviz.graphviz.RenderFormat;
import ru.urfu.lrviz.graphviz.RenderParameters;

import java.util.Objects;

/**
 *
 * @author fenya
 * @since 28.02.2026
 */
@RestController
@Tag(name = "Генерация графов LR-автоматов")
public class AutomatonGraphRenderController {
    public static final String DEFAULT_IMAGE_SIZE = "1024";

    private final ConversionService conversionService;
    private final LRAutomationGraphRenderer renderer;

    public AutomatonGraphRenderController(ConversionService conversionService, LRAutomationGraphRenderer renderer) {
        this.conversionService = conversionService;
        this.renderer = renderer;
    }

    @PostMapping(
            value = "/render",
            produces = {
                    MediaType.IMAGE_PNG_VALUE,
            },
            version = VersionsConstants.FROM_V1
    )
    public ResponseEntity<StreamingResponseBody> renderGraph(
            @RequestParam(defaultValue = DEFAULT_IMAGE_SIZE) int size,
            @RequestHeader("Accept") String requiredFormat,
            @RequestBody LRAutomatonDto automatonDto
    ) {
        RenderFormat format = conversionService.convert(requiredFormat, RenderFormat.class);
        LRAutomaton automaton = conversionService.convert(automatonDto, LRAutomaton.class);

        StreamingResponseBody stream = os ->
                renderer.render(automaton, os, new RenderParameters(size, format));

        MediaType mediaType = Objects.requireNonNull(conversionService.convert(format, MediaType.class));
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(stream);
    }
}
