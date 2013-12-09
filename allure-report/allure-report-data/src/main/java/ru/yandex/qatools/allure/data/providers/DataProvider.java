package ru.yandex.qatools.allure.data.providers;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public interface DataProvider {

    public void provide(String testPack, File outputDirectory);

}
