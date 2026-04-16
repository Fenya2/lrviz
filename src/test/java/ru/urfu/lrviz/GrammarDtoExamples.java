package ru.urfu.lrviz;

import ru.urfu.lrviz.api.dto.GrammarDto;
import ru.urfu.lrviz.api.dto.RuleDto;
import ru.urfu.lrviz.core.GrammarExamples;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.urfu.lrviz.core.GrammarExamples.*;

/**
 *
 * @author fenya
 * @since 21.03.2026
 */
public class GrammarDtoExamples {
    public static final String JSON_RESOURCES_PATH = "/grammars/api";

    public static final Map<String, GrammarDto> DTO_EXAMPLES = Map.of(
            G_1, createG1Dto(),
            G_2, createG2Dto(),
            G_9, createG9Dto());

    public static GrammarDto getAsDto(String name) {
        return DTO_EXAMPLES.get(name);
    }

    public static String getAsBuildRequestBodyFor(String grammarName) {
        try {
            URL resource = Objects.requireNonNull(GrammarExamples.class.getResource(getResourcePath(grammarName)));
            Path path = Paths.get(resource.toURI());
            return Files.readString(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getResourcePath(String grammarName) {
        return JSON_RESOURCES_PATH + "/" + grammarName + ".json";
    }

    /**
     * <pre>
     * D => T L
     * T => i | r
     * L => L ; a | a
     * </pre>
     */
    private static GrammarDto createG1Dto() {
        return new GrammarDto(
                List.of("i", "r", ";", "a"),
                List.of("D", "T", "L"),
                List.of(new RuleDto("D", "TL"),
                        new RuleDto("T", "i"),
                        new RuleDto("T", "i"),
                        new RuleDto("L", "L;a"),
                        new RuleDto("L", "a")),
                "D"
        );
    }

    private static GrammarDto createG2Dto() {
        return new GrammarDto(
                List.of("a", "b"),
                List.of("S", "A"),
                List.of(
                        new RuleDto("S", "AA"),
                        new RuleDto("A", "aA"),
                        new RuleDto("A", "b")),
                "S"
        );
    }

    private static GrammarDto createG9Dto() {
        return new GrammarDto(
                List.of("=", "*", "x"),
                List.of("S", "L", "R"),
                List.of(new RuleDto("S", "L=R"),
                        new RuleDto("S", "R"),
                        new RuleDto("L", "*R"),
                        new RuleDto("L", "x"),
                        new RuleDto("R", "L")),
                "S"
        );
    }
}
