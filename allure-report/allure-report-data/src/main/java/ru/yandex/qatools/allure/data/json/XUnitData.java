package ru.yandex.qatools.allure.data.json;

import ru.yandex.qatools.allure.data.AllureXUnit;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */
public class XUnitData implements JSONObject {

    public static final String FILE_NAME = "xunit.json";

    private AllureXUnit allureXUnit;

    public XUnitData(AllureXUnit allureXUnit) {
        this.allureXUnit = allureXUnit;
    }

    @Override
    public void serialize(File outputDirectory) {
        AllureReportUtils.serialize(outputDirectory, FILE_NAME, allureXUnit);
    }
}
