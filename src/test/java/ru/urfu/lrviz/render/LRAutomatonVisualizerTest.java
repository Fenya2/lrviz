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

    @Autowired
    LRAutomatonVisualizerTest(BuildContextCreator contextCreator,
                              LRAutomatonBuilders builders,
                              LRAutomatonVisualizer visualizer) {
        this.contextCreator = contextCreator;
        this.builders = builders;
        this.visualizer = visualizer;
    }

    @Test
    void visualizeBuildLog() throws IOException {
        Grammar grammar = GrammarExamples.get(G_1);
        BuildContext context = contextCreator.createContext(LR_1, grammar, BuildOptions.createEmpty());
        builders.build(grammar, LR_1, context);
        Path renderPath = tempDir.resolve("buildLog.zip");
        visualizeBuildLog(renderPath, context.getBuildLog());
    }

    private void visualizeBuildLog(Path renderPath, BuildLog buildLog) throws IOException {
        try (OutputStream os = Files.newOutputStream(renderPath)) {
            visualizer.visualizeBuildLog(buildLog, os, RENDER_PNG);
            Assertions.assertTrue(Files.exists(renderPath));
        }
    }
}