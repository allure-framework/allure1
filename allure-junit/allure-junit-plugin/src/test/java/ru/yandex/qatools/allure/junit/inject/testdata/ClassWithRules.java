package ru.yandex.qatools.allure.junit.inject.testdata;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.12.13
 */
public class ClassWithRules {
    @Rule
    public TemporaryFolder folder;

    @ClassRule
    public static TestName testName;
}
