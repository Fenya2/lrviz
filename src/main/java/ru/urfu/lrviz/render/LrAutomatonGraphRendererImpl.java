package ru.urfu.lrviz.render;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
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
import ru.urfu.lrviz.core.lr.LRState;

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
 * Рендерит граф по LR-автоматам
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
    public void render(LRAutomaton automaton, OutputStream outputStream, VisualizeParameters parameters) throws IOException {
        Graph graph = graph()
                .directed()
                .graphAttr()
                .with(Rank.dir(LEFT_TO_RIGHT))
                .with(createNodes(automaton, parameters.highLightBaseItems()));
        String dotView = Graphviz
                .fromGraph(graph)
                .height(parameters.size())
                .render(Format.DOT)
                .toString();
        render(dotView, outputStream);
    }

    private List<? extends LinkSource> createNodes(LRAutomaton automaton, boolean highlightBaseItems) {
        List<LinkSource> nodes = new ArrayList<>(automaton.namedStates().size());
        for (Map.Entry<String, LRState> entry : automaton.namedStates().entrySet()) {
            String stateName = entry.getKey();
            Node node = node(stateName).with(Shape.M_RECORD)
                    .with(Label.html(lrStateToHtml(stateName, entry.getValue(), highlightBaseItems)))
                    .link(addTransitions(stateName, automaton.transitions()));
            nodes.add(node);
        }
        return nodes;
    }

    private static String lrStateToHtml(String stateName, LRState state, boolean highlightBaseItems) {
        TableTag node = table()
                .attr("border", "0")
                .attr("bgcolor", LR_STATE_BACKGROUND_COLOR).with(
                        Stream.concat(
                                createTitleTag(stateName),
                                createItemTags(state, highlightBaseItems)));
        return node.render();
    }

    private static Stream<? extends DomContent> createTitleTag(String stateName) {
        return Stream.of(tr().with(td()
                .attr("bgcolor", LR_STATE_TITLE_BACKGROUND_COLOR)
                .attr("align", "center")
                .with(tag("font").withText(stateName).attr("color", LR_STATE_BACKGROUND_COLOR))));
    }

    private static Stream<? extends DomContent> createItemTags(LRState state, boolean highlightBaseItems) {
        return state.items().stream()
                .map(item -> {
                    ContainerTag<? extends Tag<?>> itemTag = tag("font").withText(item.asString());
                    if (highlightBaseItems && !item.isDotSymbolAtTheBeginning()) {
                        itemTag.attr("color", KERNEL_ITEM_COLOR);
                    }
                    return tr().with(td().attr("align", "left").with(itemTag));
                });
    }

    private List<? extends LinkTarget> addTransitions(String
                                                              stateName, Map<LRAutomaton.TransitionKey, String> transitions) {
        return transitions.entrySet().stream()
                .filter(transition -> transition.getKey().stateName().equals(stateName))
                .map(transition -> to(node(transition.getValue()))
                        .with(Label.of(transition.getKey().symbol().asString()))
                        .with(Style.lineWidth(TRANSITION_ARROW_WIDTH)))
                .toList();
    }

    private static void render(String dotView, OutputStream outputStream) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng");
        pb.redirectErrorStream(true);
        Process process = pb.start();
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
