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
import ru.urfu.lrviz.core.lr.*;

/**
 * @author fenya
 * @since 02.02.2026
 */
@RestController
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

    @PostMapping(value = "/lr0", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(0)-автомат")
    public LrBuildResultDto buildLR0(@RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR0Context();
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_0, context);
        return buildResultMapper.map(automaton, context);
    }

    @PostMapping(value = "/lr1", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Строит LR(0)-автомат")
    public LrBuildResultDto buildLR1(@RequestBody GrammarDto grammar) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = contextCreator.createLR1Context(targetGrammar);
        LRAutomaton automaton = builders.build(targetGrammar, AutomatonType.LR_1, context);
        return buildResultMapper.map(automaton, context);
    }
}
