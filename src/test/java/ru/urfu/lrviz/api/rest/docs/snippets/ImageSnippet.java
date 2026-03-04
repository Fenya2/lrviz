package ru.urfu.lrviz.api.rest.docs.snippets;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.snippet.SnippetException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Ожидает в ответе картинку и сохраняет ее c указанным именем в директорию {snippets}/images с расширением png
 *
 * @author fenya
 * @since 04.03.2026
 */
public class ImageSnippet implements Snippet {
    public static final Path IMAGES_PATH = Path.of("images");

    private final Path path;

    /**
     * @param name уникальное имя картинки
     */
    public static ImageSnippet responseImagePng(String name) {
        return new ImageSnippet(Path.of(name + ".png"));
    }


    private ImageSnippet(Path path) {
        this.path = path;
    }

    @Override
    public void document(Operation operation) throws IOException {
        RestDocumentationContext context = getDocumentationContext(operation);
        Path imagesDirectory = context.getOutputDirectory().toPath().resolve(IMAGES_PATH);
        Files.createDirectories(imagesDirectory);
        Files.write(imagesDirectory.resolve(path), operation.getResponse().getContent());
    }

    private RestDocumentationContext getDocumentationContext(Operation operation) {
        RestDocumentationContext attribute = (RestDocumentationContext) operation.getAttributes().get(RestDocumentationContext.class.getName());
        if (attribute == null) {
            throw new SnippetException("Can't get rest documentation context");
        }
        return attribute;
    }
}
