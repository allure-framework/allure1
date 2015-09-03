package ru.yandex.qatools.allure.command;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.cal10n.LocLogger;
import ru.yandex.qatools.allure.logging.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@Command(name = "generate", description = "Generate report")
public class ReportGenerate extends ReportCommand {

    private static final LocLogger LOGGER = getLogger(ReportGenerate.class);

    public static final String CLASS_PATH = "-cp";

    @Arguments(title = "Results directories", required = true,
            description = "A list of input directories or globs to be processed")
    public List<String> results = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() throws IOException, AllureCommandException {
        Path bundleJarPath = getBundleJarPath();

        if (Files.notExists(bundleJarPath)) {
            LOGGER.error(Messages.COMMAND_REPORT_GENERATE_BUNDLE_MISSING, bundleJarPath.toAbsolutePath());
            return;
        }

        if (results.isEmpty()) {
            LOGGER.error(Messages.COMMAND_REPORT_GENERATE_RESULTS_MISSING);
            return;
        }

        CommandLine commandLine = createCommandLine();
        new DefaultExecutor().execute(commandLine);

        LOGGER.info(Messages.COMMAND_REPORT_GENERATE_REPORT_GENERATED, getReportDirectoryPath());
    }

    private Path getBundleJarPath() {
        return getProperties().getAllureHome().resolve("app").resolve("allure-bundle.jar");
    }

    private Path getConfigPath() {
        return getProperties().getAllureConfig();
    }

    private Path getPluginsPath() {
        return getProperties().getAllureHome().resolve("plugins");
    }

    private String getClassPathArgument() {
        return String.format("%s:%s:%s/*",
                getConfigPath().toAbsolutePath().getParent(),
                getBundleJarPath().toAbsolutePath(),
                getPluginsPath().toAbsolutePath()

        );
    }

    private String getMainClassArgument() {
        return "ru.yandex.qatools.allure.AllureMain";
    }

    /**
     * Create a {@link CommandLine} to run bundle with needed arguments.
     *
     * @throws AllureCommandException if any occurs.
     */
    private CommandLine createCommandLine() throws AllureCommandException {
        List<String> arguments = new ArrayList<>();
        arguments.addAll(getBundleJavaOptsArgument());
        arguments.add(getLoggerConfigurationArgument());
        arguments.add(CLASS_PATH);
        arguments.add(getClassPathArgument());
        arguments.add(getMainClassArgument());
        arguments.addAll(results);
        arguments.add(getReportDirectoryPath().toString());

        return new CommandLine(getJavaExecutablePath().toString())
                .addArguments(arguments.toArray(new String[arguments.size()]));
    }


    /**
     * Get argument to configure log level for bundle.
     */
    private String getLoggerConfigurationArgument() {
        return String.format("-Dorg.slf4j.simpleLogger.defaultLogLevel=%s",
                isQuiet() || !isVerbose() ? "error" : "debug");
    }

    private List<String> getBundleJavaOptsArgument() {
        return Arrays.asList(getProperties().getBundleJavaOpts().split(" "));
    }

    /**
     * Returns the path to java executable.
     *
     * @return the path to java executable.
     * @throws NullPointerException if java.home system variable is not set.
     */
    private Path getJavaExecutablePath() {
        Path javaHome = getProperties().getJavaHome();
        if (javaHome == null) {
            throw new NullPointerException("'java.home' is not set");
        }
        return getProperties().getJavaHome().resolve("bin/java");
    }

}
