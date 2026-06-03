package ru.urfu.lrviz.render;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.urfu.lrviz.core.GrammarExamples;
import ru.urfu.lrviz.core.grammar.Grammar;
import ru.urfu.lrviz.core.lr.BuildContext;
import ru.urfu.lrviz.core.lr.BuildContextCreator;
import ru.urfu.lrviz.core.lr.BuildOptions;
import ru.urfu.lrviz.core.lr.LRAutomatonBuilders;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.urfu.lrviz.core.GrammarExamples.G_1;
import static ru.urfu.lrviz.core.lr.AutomatonType.LR_1;
import static ru.urfu.lrviz.render.VisializeParametersConstants.RENDER_PNG;

@SpringBootTest
class LRAutomatonVisualizerTest {
    @TempDir(cleanup = CleanupMode.DEFAULT)
    private Path tempDir;

    private final BuildContextCreator contextCreator;
    private final LRAutomatonBuilders builders;
    private final LRAutomatonVisualizer visualizer;
    private final VisualizationContextCreator visualizationContextCreator;

    @Autowired
    LRAutomatonVisualizerTest(BuildContextCreator contextCreator,
                              LRAutomatonBuilders builders,
                              LRAutomatonVisualizer visualizer, VisualizationContextCreator visualizationContextCreator) {
        this.contextCreator = contextCreator;
        this.builders = builders;
        this.visualizer = visualizer;
        this.visualizationContextCreator = visualizationContextCreator;
    }

    @Test
    void visualizeBuildLog() throws IOException {
        Grammar grammar = GrammarExamples.get(G_1);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        builders.build(grammar, LR_1, context);
        Path renderPath = tempDir.resolve("buildLog.zip");
        visualizeBuildLog(renderPath, context);
    }

    private void visualizeBuildLog(Path renderPath, BuildContext context) throws IOException {
        try (OutputStream os = Files.newOutputStream(renderPath)) {
            visualizer.visualizeBuildLog(context.getBuildLog(), os, visualizationContextCreator.create(RENDER_PNG, context));
            Assertions.assertTrue(Files.exists(renderPath));
        }
    }
}