package ru.urfu.lrviz.render;

import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.TransitionEntry;
import ru.urfu.lrviz.core.lr.TransitionSymbol;

import java.util.Map;
import java.util.Set;

/**
 * Контекст визуализации автомата
 *
 * @author fenya
 * @since 20.04.2026
 */
public class VisualizationContext {
    private final VisualizeOptions visualizeOptions;
    private final Set<TransitionEntry> originTransitions;
    private final LRItem startItem;
    private final ColorGenerator colorGenerator;
    private final Map<TransitionSymbol, String> colorizedSymbols;

    public VisualizationContext(VisualizeOptions visualizeOptions, Set<TransitionEntry> originTransitions, LRItem startItem, ColorGenerator colorGenerator, Map<TransitionSymbol, String> colorizedSymbols) {
        this.visualizeOptions = visualizeOptions;
        this.originTransitions = originTransitions;
        this.startItem = startItem;
        this.colorGenerator = colorGenerator;
        this.colorizedSymbols = colorizedSymbols;
    }

    public VisualizeOptions getVisualizeOptions() {
        return visualizeOptions;
    }

    public Set<TransitionEntry> getOriginTransitions() {
        return originTransitions;
    }

    public LRItem getStartItem() {
        return startItem;
    }

    public ColorGenerator getColorGenerator() {
        return colorGenerator;
    }

    public Map<TransitionSymbol, String> getColorizedSymbols() {
        return colorizedSymbols;
    }
}
