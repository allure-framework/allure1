package ru.yandex.qatools.allure.command;

import io.airlift.command.Help;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class AllureHelp extends Help implements AllureCommand {

    @Override
    public ExitCode getExitCode() {
        return ExitCode.NO_ERROR;
    }
}
