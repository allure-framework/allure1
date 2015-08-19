package ru.yandex.qatools.allure.logging;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
@BaseName("messages")
@LocaleData(value = {@Locale("en"), @Locale("ru")}, defaultCharset = "UTF-8")
public enum Messages {

    //Common commands
    COMMANDLINE_ERROR,

    //Common commands
    COMMAND_ALLURE_COMMAND_ABORTED,

    //Report command
    COMMAND_REPORT_CLEAN_REPORT_CLEANED,
    COMMAND_REPORT_CLEAN_ITEM_DELETED,

    COMMAND_REPORT_GENERATE_BUNDLE_MISSING,
    COMMAND_REPORT_GENERATE_REPORT_GENERATED,
    COMMAND_REPORT_GENERATE_RESULTS_MISSING,

    COMMAND_REPORT_OPEN_REPORT_MISSING,
    COMMAND_REPORT_OPEN_SERVER_STARTING,
    COMMAND_REPORT_OPEN_SERVER_STARTED,

    COMMAND_REPORT_CANT_OPEN_BROWSER,

    //Version command
    COMMAND_VERSION_INFO

}
