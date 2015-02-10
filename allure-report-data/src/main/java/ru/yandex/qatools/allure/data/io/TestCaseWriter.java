package ru.yandex.qatools.allure.data.io;

import ru.yandex.qatools.allure.data.AllureTestCase;

import javax.inject.Inject;
import java.io.File;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;

/**
 * eroshenkoam
 * 03/02/15
 */
public class TestCaseWriter implements Writer<AllureTestCase> {

    @Inject
    @ReportDirectory
    private File reportDirectory;

    @Override
    public int write(AllureTestCase allureTestCase) {
        return serialize(reportDirectory, allureTestCase.getUid() + "-testcase.json", allureTestCase);
    }
}
