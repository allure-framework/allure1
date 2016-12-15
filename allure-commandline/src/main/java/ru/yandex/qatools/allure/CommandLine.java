package ru.yandex.qatools.allure;

import io.airlift.airline.Cli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.command.AbstractCommand;
import ru.yandex.qatools.allure.command.AllureCommand;
import ru.yandex.qatools.allure.command.AllureCommandException;
import ru.yandex.qatools.allure.command.AllureHelp;
import ru.yandex.qatools.allure.command.AllureVersion;
import ru.yandex.qatools.allure.command.ExitCode;
import ru.yandex.qatools.allure.command.ReportClean;
import ru.yandex.qatools.allure.command.ReportGenerate;
import ru.yandex.qatools.allure.command.ReportOpen;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class CommandLine {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);

    private CommandLine() {
    }

    public static void main(String[] args) throws InterruptedException {
        ExitCode exitCode;
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
        } catch (AllureCommandException e) {
            LOGGER.error("{}", e);
            exitCode = ExitCode.GENERIC_ERROR;
        } catch (Exception e) {
            LOGGER.error("{}", e);
            exitCode = ExitCode.ARGUMENT_PARSING_ERROR;
        }

        System.exit(exitCode.getCode());
    }
}
