package ru.urfu.lrviz.graphviz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.BuildContextCreator;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRAutomatonBuilders;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.urfu.lrviz.core.GrammarExamples.G_2;
import static ru.urfu.lrviz.core.lr.AutomatonType.LR_0;
import static ru.urfu.lrviz.core.lr.AutomatonType.LR_1;

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
class LRAutomationGraphRendererTest {
    private static final RenderParameters RENDER_PNG = new RenderParameters(1000, RenderFormat.PNG);

    @TempDir(cleanup = CleanupMode.DEFAULT)
    Path tempDir;

    private final LRAutomatonBuilders builders;
    private final BuildContextCreator contextCreator;
    private final LRAutomationGraphRendererImpl renderer;

    @Autowired
    LRAutomationGraphRendererTest(LRAutomatonBuilders builders,
                                  BuildContextCreator contextCreator,
                                  LRAutomationGraphRendererImpl renderer) {
        this.builders = builders;
        this.contextCreator = contextCreator;
        this.renderer = renderer;
    }

    @Test
    void renderLR0() throws IOException {
        LRAutomaton automaton = builders.build(
                GrammarExamples.get(G_2),
                LR_0,
                contextCreator.createLR0Context());
        Path renderPath = tempDir.resolve("renderLR0.png");
        render(renderPath, automaton);
    }

    @Test
    void renderLR1() throws IOException {
        Grammar grammar = GrammarExamples.get(G_2);
        LRAutomaton automaton = builders.build(
                grammar,
                LR_1,
                contextCreator.createLR1Context(grammar));
        Path renderPath = tempDir.resolve("renderLR1.png");
        render(renderPath, automaton);
    }

    private void render(Path renderPath, LRAutomaton automaton) throws IOException {
        try (OutputStream os = Files.newOutputStream(renderPath)) {
            renderer.render(automaton, os, RENDER_PNG);
            Assertions.assertTrue(Files.exists(renderPath));
        }
    }
}