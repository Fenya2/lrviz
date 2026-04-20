package ru.urfu.lrviz.render;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.*;

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
    private final VisualizationContextCreator visualizationContextCreator;

    @Autowired
    LRAutomationGraphRendererImplTest(LRAutomatonBuilders builders,
                                      BuildContextCreator contextCreator,
                                      LrAutomatonGraphRendererImpl renderer, VisualizationContextCreator visualizationContextCreator) {
        this.builders = builders;
        this.contextCreator = contextCreator;
        this.renderer = renderer;
        this.visualizationContextCreator = visualizationContextCreator;
    }

    @Test
    void renderLR0() throws IOException {
        Grammar grammar = GrammarExamples.get(G_2);
        BuildContext context = contextCreator.createContext(LR_0, grammar, BuildOptions.createEmpty());
        LRAutomaton automaton = builders.build(grammar, LR_0, context);
        Path renderPath = tempDir.resolve("renderLR0.png");
        render(renderPath, automaton, context);
    }

    @Test
    void renderLR1() throws IOException {
        Grammar grammar = GrammarExamples.get(G_2);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        LRAutomaton automaton = builders.build(grammar, LR_1, context);
        Path renderPath = tempDir.resolve("renderLR1.png");
        render(renderPath, automaton, context);
    }

    @Test
    void renderLALR1() throws IOException {
        Grammar grammar = GrammarExamples.get(G_2);
        BuildContext context = contextCreator.createContext(LALR, grammar, BuildOptions.createEmpty());
        LRAutomaton automaton = builders.build(grammar, LALR, context);
        Path renderPath = tempDir.resolve("renderLALR1.png");
        render(renderPath, automaton, context);
    }

    private void render(Path renderPath, LRAutomaton automaton, BuildContext buildContext) throws IOException {
        try (OutputStream os = Files.newOutputStream(renderPath)) {
            renderer.render(automaton, os, visualizationContextCreator.create(RENDER_PNG, buildContext));
            Assertions.assertTrue(Files.exists(renderPath));
        }
    }
}