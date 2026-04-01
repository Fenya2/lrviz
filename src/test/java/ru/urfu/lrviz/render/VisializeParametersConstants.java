package ru.urfu.lrviz.render;

/**
 *
 * @author fenya
 * @since 31.03.2026
 */
public class VisializeParametersConstants {
    public static final VisualizeParameters RENDER_PNG = VisualizeParameters.builder()
            .size(1000)
            .renderFormat(RenderFormat.PNG)
            .highLightBaseItems(false)
            .build();
}
