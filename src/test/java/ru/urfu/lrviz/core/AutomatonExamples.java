package ru.urfu.lrviz.core;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 *
 * @author fenya
 * @since 28.02.2026
 */
public class AutomatonExamples {
    private static final String RENDERS_PATH = "/graphviz/renders";
    private static final String LR0_RENDERS_PATH = RENDERS_PATH + "/lr0";

    public static byte[] getExpectedRenderedLr0InPng(String grammarName) {
        try {
            String resourcePath = LR0_RENDERS_PATH + "/" + grammarName + ".png";
            URL resource = Objects.requireNonNull(GrammarExamples.class.getResource(resourcePath));
            Path path = Paths.get(resource.toURI());
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
