package ru.urfu.lrviz.render;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.lr.BuildLog;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRAutomatonReconstructor;
import ru.urfu.lrviz.core.lr.operations.BuildOperation;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author fenya
 * @since 31.03.2026
 */
@Service
public class LRAutomatonVisualizer {
    private final LRAutomationGraphRenderer renderer;
    private final LRAutomatonReconstructor reconstructor;

    public LRAutomatonVisualizer(LRAutomationGraphRenderer renderer, LRAutomatonReconstructor reconstructor) {
        this.renderer = renderer;
        this.reconstructor = reconstructor;
    }

    public void visualize(LRAutomaton automaton, OutputStream outputStream, VisualizeParameters parameters) throws IOException {
        renderer.render(automaton, outputStream, parameters);
    }

    public void visualizeBuildLog(BuildLog buildLog, OutputStream outputStream, VisualizeParameters parameters) throws IOException {
        List<BuildOperation> operations = buildLog.getOperations();
        try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            for (int i = 0; i < operations.size(); i++) {
                LRAutomaton reconstructed = reconstructor.reconstructUntil(buildLog, i);
                String entryName = String.format("%d.png", i);
                ZipEntry entry = new ZipEntry(entryName);
                zip.putNextEntry(entry);
                renderer.render(reconstructed, wrapInNonClosingStream(zip), parameters);
                zip.closeEntry();
            }
        }
    }

    private static OutputStream wrapInNonClosingStream(ZipOutputStream zip) {
        return new FilterOutputStream(zip) {
            @Override
            public void close() throws IOException {
                flush();
            }
        };
    }
}
