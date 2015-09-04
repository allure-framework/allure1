package ru.yandex.qatools.allure.command;

import io.airlift.command.Command;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.slf4j.cal10n.LocLogger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_CANT_OPEN_BROWSER;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_OPEN_REPORT_MISSING;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_OPEN_SERVER_STARTED;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_OPEN_SERVER_STARTING;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@Command(name = "open", description = "Open generated report")
public class ReportOpen extends ReportCommand {

    private static final LocLogger LOGGER = getLogger(ReportOpen.class);

    @Override
    protected void runUnsafe() throws Exception {
        Path reportDirectory = getReportDirectoryPath();
        if (Files.notExists(reportDirectory)) {
            throw new AllureCommandException(COMMAND_REPORT_OPEN_REPORT_MISSING, reportDirectory);
        }

        LOGGER.info(COMMAND_REPORT_OPEN_SERVER_STARTING, reportDirectory);
        Server server = setUpServer();
        server.start();

        openBrowser(server.getURI());
        LOGGER.info(COMMAND_REPORT_OPEN_SERVER_STARTED, server.getURI());
        server.join();
    }

    /**
     * Open the given url in default system browser.
     */
    private void openBrowser(URI url) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(url);
        } else {
            LOGGER.error(COMMAND_REPORT_CANT_OPEN_BROWSER);
        }
    }

    /**
     * Set up server for report directory.
     */
    private Server setUpServer() {
        Server server = new Server(0);
        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setWelcomeFiles(new String[]{"index.html"});
        handler.setResourceBase(getReportDirectoryPath().toAbsolutePath().toString());
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler, new DefaultHandler()});
        server.setStopAtShutdown(true);
        server.setHandler(handlers);
        return server;
    }
}