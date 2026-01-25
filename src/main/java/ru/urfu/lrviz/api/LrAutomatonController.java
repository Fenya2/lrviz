package ru.urfu.lrviz.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/automaton")
public class LrAutomatonController {

    private final ConversionService conversionService;
    private final LRAutomatonBuilders automatonsBuilders;

    @Autowired
    public LrAutomatonController(ConversionService conversionService, LRAutomatonBuilders automatonsBuilders) {
        this.conversionService = conversionService;
        this.automatonsBuilders = automatonsBuilders;
    }

    @PostMapping(value = "/build",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buildAutomaton(
            @RequestParam AutomatonType type,
            @RequestBody GrammarDto grammar
    ) {
        Grammar targetGrammar = conversionService.convert(grammar, Grammar.class);
        automatonsBuilders.build(targetGrammar, type, new BuildContext(new BuildLog()));
        return null;
    }
}
