package ru.urfu.lrviz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Выполняет код, который требуется запустить сразу после старта приложения
 */
@Component
public class StartupListener {
    private static final Logger LOG = LoggerFactory.getLogger(StartupListener.class);

    /**
     * Запущено ли приложение локально для индивидуального использования
     */
    private final boolean localStartup;
    private final int appPort;

    public StartupListener(@Value("${lrviz.local-run}") boolean localStartup,
                           @Value("${server.port}") int appPort) {
        this.localStartup = localStartup;
        this.appPort = appPort;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void tryOpenApp() throws IOException {
        if (!localStartup
                || !Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            return;
        }
        URI url;
        try {
            url = new URI("http://localhost:" + appPort + "/");
        } catch (URISyntaxException e) {
            LOG.warn("Unexpected web context equals null");
            return;
        }
        Desktop.getDesktop().browse(url);
    }
}
