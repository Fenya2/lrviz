package ru.urfu.lrviz.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.convert.AutomatonConverter;
import ru.urfu.lrviz.core.automaton.DFA;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.grammar.GrammarSymbol;
import ru.urfu.lrviz.core.lr.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/automaton")
public class LrAutomatonController {
    private final AutomatonConverter automatonConverter;
    private final ConversionService conversionService;
    private final LRAutomatonBuilders automatonsBuilders;

    @Autowired
    public LrAutomatonController(
            ConversionService conversionService,
            LRAutomatonBuilders automatonsBuilders,
            AutomatonConverter automatonConverter) {
        this.conversionService = conversionService;
        this.automatonsBuilders = automatonsBuilders;
        this.automatonConverter = automatonConverter;
    }

    @PostMapping(value = "/build",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buildAutomaton(
            @RequestParam AutomatonType type,
            @RequestBody GrammarDto grammar
    ) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        BuildContext context = new BuildContext(new BuildLog());
        DFA<? extends LRAutomatonState, GrammarSymbol> automaton = automatonsBuilders.build(targetGrammar, type, context);
        return ResponseEntity.ok(automatonConverter.toDto(automaton));
    }
}
