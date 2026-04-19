package ru.urfu.lrviz.api.docs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static ru.urfu.lrviz.api.docs.OpenApiConfig.DETAILED_API_DOCS_PATH;

/**
 * @author fenya
 * @since 19.04.2026
 */
@Controller
public class DocsController {
    @GetMapping({"api/docs", "/api/docs/"})
    public ResponseEntity<Void> redirectToGuide() {
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .header(HttpHeaders.LOCATION, DETAILED_API_DOCS_PATH)
                .build();
    }
}