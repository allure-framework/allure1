package ru.yandex.qatools.allure.junit.inject;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.inject.Injector;
import ru.yandex.qatools.allure.junit.TestCaseReportRule;
import ru.yandex.qatools.allure.junit.TestSuiteReportRule;
import ru.yandex.qatools.allure.junit.inject.testdata.*;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.09.13
 */

@RunWith(Parameterized.class)
public class ShouldAddRulesTest {

    private Class<?> origin;

    private Class<?> modify;

    private Object modifiedTest;

    public ShouldAddRulesTest(Class<?> origin) {
        this.origin = origin;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{TestClass.class},
                new Object[]{TestClassWithStaticConstructor.class},
                new Object[]{TheoryTestClass.class},
                new Object[]{TestClassWithRules.class},
                new Object[]{TestClassWithNonAnnotatedRules.class}
        );
    }

    @Before
    public void injectAllureRules() throws Exception {
        modify = Injector.defineClass(origin.getName(), new JUnitRulesInjector().inject(origin));
        modifiedTest = modify.newInstance();
    }

    @Test
    public void checkThatTestSuiteRuleInjectedSuccessfully() throws Exception {
        Field testSuiteRule = modify.getField(JUnitRulesInjector.TEST_SUITE_RULE_FIELD_NAME);
        assertThat(origin.getName(), testSuiteRule.getType(), equalTo((Class) TestSuiteReportRule.class));
        assertThat(origin.getName(), testSuiteRule.getAnnotation(ClassRule.class), notNullValue());
        assertThat(origin.getName(), testSuiteRule.get(modifiedTest), notNullValue());
    }

    @Test
    public void testCaseRuleInjectedSuccessfullyTest() throws Exception {
        Field testCaseRule = modify.getField(JUnitRulesInjector.TEST_CASE_RULE_FIELD_NAME);
        assertThat(origin.getName(), testCaseRule.getType(), equalTo((Class) TestCaseReportRule.class));
        assertThat(origin.getName(), testCaseRule.getAnnotation(Rule.class), notNullValue());
        assertThat(origin.getName(), testCaseRule.get(modifiedTest), notNullValue());
    }
}
