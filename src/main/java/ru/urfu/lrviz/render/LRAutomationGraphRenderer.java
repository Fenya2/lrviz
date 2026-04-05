package ru.urfu.lrviz.render;

import ru.urfu.lrviz.core.lr.LRAutomaton;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author fenya
 * @since 28.02.2026
 */
public interface LRAutomationGraphRenderer {
    void render(LRAutomaton automaton, OutputStream outputStream, VisualizeOptions parameters) throws IOException;
}
