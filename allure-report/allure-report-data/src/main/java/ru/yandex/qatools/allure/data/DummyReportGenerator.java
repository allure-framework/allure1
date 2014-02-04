package ru.yandex.qatools.allure.data;

import ru.yandex.qatools.allure.data.AllureReportGenerator;

import java.io.File;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/22/14
 */
public class DummyReportGenerator {

    public static void main(String[] args) {
        AllureReportGenerator reportGenerator = new AllureReportGenerator(new File(args[0]));
        reportGenerator.generate(new File(args[1]));
    }
}
