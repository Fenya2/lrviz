package ru.urfu.lrviz.render;

import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.lr.BuildContext;

import java.util.HashMap;

/**
 *
 * @author fenya
 * @since 20.04.2026
 */
@Service
public class VisualizationContextCreator {
    /**
     * Создает контекст для визуализации автомата
     *
     * @param options      параметры визуализации
     * @param buildContext контекст построения автомата
     */
    public VisualizationContext create(VisualizeOptions options, BuildContext buildContext) {
        return new VisualizationContext(
                options,
                buildContext.getOriginTransitions(),
                buildContext.getStartItem(),
                getColorGenerator(options),
                new HashMap<>());
    }

    private ColorGenerator getColorGenerator(VisualizeOptions options) {
        return options.isColorizeTransitions()
                ? new FiniteCycledContrastColorGenerator()
                : BlackColorGenerator.create();
    }
}
