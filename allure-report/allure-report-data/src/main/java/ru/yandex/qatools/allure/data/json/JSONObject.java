package ru.yandex.qatools.allure.data.json;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */
public interface JSONObject {

    public void serialize(File outputDirectory);
}
