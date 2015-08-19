package ru.yandex.qatools.allure;

import io.airlift.command.Cli;
import org.slf4j.cal10n.LocLogger;
import ru.yandex.qatools.allure.command.ReportClean;
import ru.yandex.qatools.allure.command.ReportOpen;
import ru.yandex.qatools.allure.command.ReportGenerate;
import ru.yandex.qatools.allure.command.AbstractCommand;
import ru.yandex.qatools.allure.command.AllureCommand;
import ru.yandex.qatools.allure.command.AllureVersion;
import ru.yandex.qatools.allure.command.AllureHelp;
import ru.yandex.qatools.allure.command.ExitCode;
import ru.yandex.qatools.allure.logging.LogManager;
import ru.yandex.qatools.allure.logging.Messages;

import static ru.yandex.qatools.allure.logging.LogManager.getLogger;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class CommandLine {

    private static final LocLogger LOGGER = getLogger(AbstractCommand.class);

    private CommandLine() {
    }

    public static void main(String[] args) throws InterruptedException {
        ExitCode exitCode = ExitCode.NO_ERROR;
        try {
            Cli.CliBuilder<AllureCommand> builder = Cli.<AllureCommand>builder("allure")
                    .withDefaultCommand(AllureHelp.class)
                    .withCommand(AllureHelp.class)
                    .withCommand(AllureVersion.class)
                    .withCommand(ReportGenerate.class);

            builder.withGroup("report")
                    .withDescription("Report commands")
                    .withDefaultCommand(ReportOpen.class)
                    .withCommand(ReportOpen.class)
                    .withCommand(ReportClean.class)
                    .withCommand(ReportGenerate.class);


            Cli<AllureCommand> allureParser = builder.build();
            AllureCommand command = allureParser.parse(args);

            command.run();      //NOSONAR
            exitCode = command.getExitCode();
        } catch (Exception e) {
            LOGGER.error(Messages.COMMANDLINE_ERROR, e);
            exitCode = ExitCode.ARGUMENT_PARSING_ERROR;
        } finally {
            LogManager.shutdown();
        }
        System.exit(exitCode.getCode());
    }
}
