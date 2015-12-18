package ru.yandex.qatools.allure.command;

import io.airlift.command.Arguments;
import io.airlift.command.Command;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.cal10n.LocLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_GENERATE_BUNDLE_MISSING;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_GENERATE_REPORT_GENERATED;
import static ru.yandex.qatools.allure.logging.Message.COMMAND_REPORT_GENERATE_RESULT_DIRECTORY_MISSING;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@Command(name = "generate", description = "Generate report")
public class ReportGenerate extends ReportCommand {

    private static final LocLogger LOGGER = getLogger(ReportGenerate.class);

    public static final String CLASS_PATH = "-cp";

    public static final String MAIN = "ru.yandex.qatools.allure.AllureMain";

    public static final String WIN = "win";

    @Arguments(title = "Results directories", required = true,
            description = "A list of input directories to be processed")
    public List<String> results = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void runUnsafe() throws Exception {
        validateResultsDirectories();
        CommandLine commandLine = createCommandLine();
        new DefaultExecutor().execute(commandLine);
        LOGGER.info(COMMAND_REPORT_GENERATE_REPORT_GENERATED, getReportDirectoryPath());
    }

    /**
     * Throws an exception if at least one results directory is missing.
     */
    protected void validateResultsDirectories() throws AllureCommandException {
        for (String result : results) {
            if (Files.notExists(Paths.get(result))) {
                throw new AllureCommandException(COMMAND_REPORT_GENERATE_RESULT_DIRECTORY_MISSING, result);
            }
        }
    }

    /**
     * Format the classpath string from given classpath elements.
     */
    protected String formatClassPath(String first, String... others) {
        String result = first;
        for (String other : others) {
            result += PROPERTIES.getPathSeparator() + other;
        }
        return result;
    }

    /**
     * Create a {@link CommandLine} to run bundle with needed arguments.
     */
    private CommandLine createCommandLine() throws AllureCommandException, IOException {
        return new CommandLine(getJavaExecutablePath().toString())
                .addArguments(getBundleJavaOptsArgument())
                .addArgument(getLoggerConfigurationArgument())
                .addArgument(CLASS_PATH)
                .addArgument(formatClassPath(getBundleJarPath(), getConfigPath(), getPluginsPath()), false)
                .addArgument(MAIN)
                .addArguments(results.toArray(new String[results.size()]), false)
                .addArgument(getReportDirectoryPath().toString(), false);
    }

    /**
     * Returns the bundle jar classpath element.
     */
    protected String getBundleJarPath() throws AllureCommandException {
        Path path = PROPERTIES.getAllureHome().resolve("app/allure-bundle.jar").toAbsolutePath();

        if (Files.notExists(path)) {
            throw new AllureCommandException(COMMAND_REPORT_GENERATE_BUNDLE_MISSING, path);
        }
        return path.toString();
    }

    /**
     * Returns the config directory classpath element.
     */
    protected String getConfigPath() throws IOException {
        Path config = createTempDirectory("config");
        if (Files.exists(PROPERTIES.getAllureConfig())) {
            Files.copy(PROPERTIES.getAllureConfig(), config.resolve("allure.properties"));
        }
        return config.toAbsolutePath().toString();
    }

    /**
     * Returns the plugins directory classpath element.
     */
    protected String getPluginsPath() {
        Path path = PROPERTIES.getAllureHome().resolve("plugins").toAbsolutePath();
        return String.format("%s/*", path);
    }

    /**
     * Get argument to configure log level for bundle.
     */
    protected String getLoggerConfigurationArgument() {
        return String.format("-Dorg.slf4j.simpleLogger.defaultLogLevel=%s",
                isQuiet() || !isVerbose() ? "error" : "debug");
    }

    /**
     * Returns the bundle java options split by space.
     */
    protected String getBundleJavaOptsArgument() {
        return PROPERTIES.getBundleJavaOpts();
    }

    /**
     * Returns the path to java executable.
     */
    protected Path getJavaExecutablePath() {
        String executableName = isWindows() ? "bin/java.exe" : "bin/java";
        return PROPERTIES.getJavaHome().resolve(executableName);
    }

    /**
     * Returns true if operation system is windows, false otherwise.
     */
    protected boolean isWindows() {
        return PROPERTIES.getOsName().contains(WIN);
    }
}
