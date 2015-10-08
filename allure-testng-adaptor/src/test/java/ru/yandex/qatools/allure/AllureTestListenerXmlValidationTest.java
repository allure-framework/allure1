package ru.yandex.qatools.allure;

import org.junit.Test;
import org.testng.TestNG;
import ru.yandex.qatools.allure.testdata.TestDataClass;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.AllureUtils.listTestSuiteXmlFiles;
import static ru.yandex.qatools.allure.AllureUtils.validateResults;

/**
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 04.02.14
 */
public class AllureTestListenerXmlValidationTest extends BasicListenerTest {

    @Test
    public void suiteFilesCountTest() throws Exception {
        assertThat(listTestSuiteXmlFiles(resultsDirectory).size(), is(1));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        validateResults(resultsDirectory);
    }

    @Override
    public void configure(TestNG testNG) {
        testNG.setTestClasses(new Class[]{TestDataClass.class});
    }
}