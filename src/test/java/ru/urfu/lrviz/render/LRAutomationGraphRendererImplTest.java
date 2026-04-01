package ru.urfu.lrviz.render;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.BuildContextCreator;
import ru.urfu.lrviz.core.lr.BuildOptions;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRAutomatonBuilders;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.urfu.lrviz.core.GrammarExamples.G_2;
import static ru.urfu.lrviz.core.lr.AutomatonType.*;
import static ru.urfu.lrviz.render.VisializeParametersConstants.RENDER_PNG;

/**
 * Тестирование рендера LR-автоматов. Так как библиотека graphviz не гарантирует детерминируемость рендеров от запуска
 * к запуску, просто проверяется наличие файла после рендера. Чтобы реально проверить результат рендера, можно
 * соответсвующие "эталонные" рендеры открыть в {@code src/test/resources/graphviz/renders}, а у аннотации над
 * {@link #tempDir} поменять режим запуска на {@link CleanupMode#NEVER} для последующего сравнения
 *
 * @author fenya
 * @since 28.02.2026
 */
@SpringBootTest
class LRAutomationGraphRendererImplTest {
    @TempDir(cleanup = CleanupMode.DEFAULT)
    private Path tempDir;

    private final LRAutomatonBuilders builders;
    private final BuildContextCreator contextCreator;
    private final LrAutomatonGraphRendererImpl renderer;

    @Autowired
    LRAutomationGraphRendererImplTest(LRAutomatonBuilders builders,
                                      BuildContextCreator contextCreator,
                                      LrAutomatonGraphRendererImpl renderer) {
        this.builders = builders;
        this.contextCreator = contextCreator;
        this.renderer = renderer;
    }

    @Test
    void renderLR0() throws IOException {
        Grammar grammar = GrammarExamples.get(G_2);
        LRAutomaton automaton = builders.build(
                grammar,
                LR_0,
                contextCreator.createContext(LR_0, grammar, BuildOptions.createEmpty()));
        Path renderPath = tempDir.resolve("renderLR0.png");
        render(renderPath, automaton);
    }

    @Test
    void renderLR1() throws IOException {
        Grammar grammar = GrammarExamples.get(G_2);
        LRAutomaton automaton = builders.build(
                grammar,
                LR_1,
                contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty()));
        Path renderPath = tempDir.resolve("renderLR1.png");
        render(renderPath, automaton);
    }

    @Test
    void renderLALR1() throws IOException {
        Grammar grammar = GrammarExamples.get(G_2);
        LRAutomaton automaton = builders.build(
                grammar,
                LALR,
                contextCreator.createContext(LALR, grammar, BuildOptions.createEmpty()));
        Path renderPath = tempDir.resolve("renderLALR1.png");
        render(renderPath, automaton);
    }

    private void render(Path renderPath, LRAutomaton automaton) throws IOException {
        try (OutputStream os = Files.newOutputStream(renderPath)) {
            renderer.render(automaton, os, RENDER_PNG);
            Assertions.assertTrue(Files.exists(renderPath));
        }
    }
}