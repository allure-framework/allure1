package ru.yandex.qatools.allure;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.testdata.TestWithTimeoutAnnotation;
import ru.yandex.qatools.allure.testdata.TestWithTimeoutRule;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
@Ignore("I don't know how to access Allure instance from TestWithTimeoutAnnotation and " +
        "TestWithTimeoutRule tests.")
@RunWith(Parameterized.class)
public class AllureListenerTimeoutTest extends BasicListenerTest {

    private Class<?> testClass;

    public AllureListenerTimeoutTest(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{TestWithTimeoutAnnotation.class},
                new Object[]{TestWithTimeoutRule.class}
        );
    }

    @Test
    public void shouldNotLoseEventsFromTestWithTimeout() throws Exception {
        TestSuiteResult result = JAXB.unmarshal(
                AllureUtils.listTestSuiteFiles(resultsDirectory).iterator().next().toFile(),
                TestSuiteResult.class
        );

        assertThat(result.getTestCases(), not(empty()));
        assertThat(
                String.format(
                        "Result should contains title '%s'",
                        TestWithTimeoutAnnotation.NAME
                ),
                result.getTestCases().iterator().next().getTitle(),
                is(TestWithTimeoutAnnotation.NAME)
        );
    }

    @Override
    public Class<?> getTestClass() {
        return testClass;
    }



}
