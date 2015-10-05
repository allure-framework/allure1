package ru.yandex.qatools.allure;

import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static ru.yandex.qatools.allure.AllureUtils.validateResults;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
public class ResultsSchemaValidationTest {

    @Test
    public void testSuiteFileValidationTest() throws Exception {
        URL url = ResultsSchemaValidationTest.class.getClassLoader().getResource("allure-results");
        Objects.requireNonNull(url);

        Path directory = Paths.get(url.toURI());
        validateResults(directory);
    }
}