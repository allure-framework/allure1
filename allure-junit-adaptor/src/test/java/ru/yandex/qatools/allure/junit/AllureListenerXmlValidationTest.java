package ru.yandex.qatools.allure.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.junit.testdata.SimpleTestClass;
import ru.yandex.qatools.allure.junit.testdata.TestClassWithExceptionInBefore;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
@RunWith(Parameterized.class)
public class AllureListenerXmlValidationTest extends BasicListenerTest {

    public Class<?> testClass;

    public AllureListenerXmlValidationTest(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                                new Object[]{SimpleTestClass.class},
                                new Object[]{TestClassWithExceptionInBefore.class}
        );
    }


    @Test
    public void suiteFilesCountTest() throws Exception {
        assertThat(listTestSuiteFiles(resultsDirectory).size(), is(1));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDirectory)) {
            validator.validate(new StreamSource(each));
        }
    }

    @Override
    public Class<?> getTestClass() {
        return testClass;
    }

}
