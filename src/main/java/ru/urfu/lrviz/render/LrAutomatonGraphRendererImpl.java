package ru.urfu.lrviz.render;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.LinkSource;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.Node;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.Tag;
import j2html.tags.specialized.TableTag;
import org.springframework.stereotype.Service;
import ru.urfu.lrviz.core.lr.LRAutomaton;
import ru.urfu.lrviz.core.lr.LRItem;
import ru.urfu.lrviz.core.lr.LRState;
import ru.urfu.lrviz.core.lr.TransitionKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;
import static j2html.TagCreator.*;

/**
 * Рендерит граф по LR-автомату
 *
 * @author fenya
 * @since 28.02.2026
 */
@Service
public class LrAutomatonGraphRendererImpl implements LRAutomationGraphRenderer {
    public static final int TRANSITION_ARROW_WIDTH = 2;
    public static final String LR_STATE_TITLE_BACKGROUND_COLOR = "black";
    public static final String LR_STATE_BACKGROUND_COLOR = "white";
    public static final String KERNEL_ITEM_COLOR = "red";

    @Override
    public void render(LRAutomaton automaton, OutputStream outputStream, VisualizationContext context) throws IOException {
        Graph graph = graph()
                .directed()
                .graphAttr()
                .with(Rank.dir(LEFT_TO_RIGHT))
                .with(createNodes(automaton, context));
        String dotView = Graphviz
                .fromGraph(graph)
                .render(Format.DOT)
                .toString();
        render(dotView, outputStream);
    }

    private List<? extends LinkSource> createNodes(LRAutomaton automaton, VisualizationContext context) {
        List<LinkSource> nodes = new ArrayList<>(automaton.namedStates().size());
        for (Map.Entry<String, LRState> entry : automaton.namedStates().entrySet()) {
            String stateName = entry.getKey();
            Node node = node(stateName).with(Shape.M_RECORD)
                    .with(Label.html(lrStateToHtml(stateName, entry.getValue(), context)))
                    .link(addTransitions(stateName, automaton.transitions(), context));
            nodes.add(node);
        }
        return nodes;
    }

    private static String lrStateToHtml(String stateName, LRState state, VisualizationContext context) {
        TableTag node = table()
                .attr("border", "0")
                .attr("bgcolor", LR_STATE_BACKGROUND_COLOR).with(
                        Stream.concat(
                                createTitleTag(stateName),
                                createItemTags(state, context)));
        return node.render();
    }

    private static Stream<? extends DomContent> createTitleTag(String stateName) {
        return Stream.of(tr().with(td()
                .attr("bgcolor", LR_STATE_TITLE_BACKGROUND_COLOR)
                .attr("align", "center")
                .with(tag("font").withText(stateName).attr("color", LR_STATE_BACKGROUND_COLOR))));
    }

    private static Stream<? extends DomContent> createItemTags(LRState state, VisualizationContext context) {
        return state.items().stream()
                .map(item -> {
                    ContainerTag<? extends Tag<?>> itemTag = tag("font").withText(item.asString());
                    if (context.getVisualizeOptions().isHighLightBaseItems() && isBaseItem(item, context)) {
                        itemTag.attr("color", KERNEL_ITEM_COLOR);
                    }
                    return tr().with(td().attr("align", "left").with(itemTag));
                });
    }

    private static boolean isBaseItem(LRItem item, VisualizationContext context) {
        return !item.isDotSymbolAtTheBeginning() || context.getStartItem().equals(item);
    }

    private List<? extends LinkTarget> addTransitions(
            String stateName, Map<TransitionKey, String> transitions, VisualizationContext context) {
        return transitions.entrySet().stream()
                .filter(transition -> transition.getKey().stateName().equals(stateName))
                .map(transition -> to(node(transition.getValue()))
                        .with(Label.of(transition.getKey().symbol().asString()))
                        .with(getTransitionColor(transition, context))
                        .with(Style.lineWidth(TRANSITION_ARROW_WIDTH)))
                .toList();
    }

    private Attributes<? extends ForLink> getTransitionColor(Map.Entry<TransitionKey, String> transition, VisualizationContext context) {
        return Color.rgb(context.getColorizedSymbols().computeIfAbsent(transition.getKey().symbol(), _ -> context.getColorGenerator().next()));
    }

    private static void render(String dotView, OutputStream outputStream) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (OutputStream stdin = process.getOutputStream()) {
            stdin.write(dotView.getBytes(StandardCharsets.UTF_8));
        }
        try (InputStream stdout = process.getInputStream()) {
            stdout.transferTo(outputStream);
        }
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Process was interrupted", e);
        }
        if (exitCode != 0) {
            throw new IOException("dot process failed with exit code: " + exitCode);
        }
    }
}
