package ru.yandex.qatools.allure.data.transform;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public interface TestRunTransformer {

    public void transform(String xml, File outputDirectory);

}
