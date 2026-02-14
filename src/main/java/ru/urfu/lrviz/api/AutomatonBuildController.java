package ru.urfu.lrviz.api;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.lr0.LR0AutomatonBuilder;

/**
 *
 * @author fenya
 * @since 02.02.2026
 */
@RestController
@RequestMapping("/build")
public class AutomatonBuildController {
    private final ConversionService conversionService;
    private final LR0AutomatonBuilder lr0Builder;
    private final LrBuildResultMapper buildResultMapper;

    public AutomatonBuildController(ConversionService conversionService, LR0AutomatonBuilder lr0Builder, LrBuildResultMapper buildResultMapper) {
        this.conversionService = conversionService;
        this.lr0Builder = lr0Builder;
        this.buildResultMapper = buildResultMapper;
    }

    @PostMapping(value = "/lr0", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(0)-автомат")
    public LrBuildResultDto buildLR0(@RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = BuildContext.create();
        LRAutomaton automaton = lr0Builder.build(targetGrammar, context);
        return buildResultMapper.map(automaton, context);
    }
}
