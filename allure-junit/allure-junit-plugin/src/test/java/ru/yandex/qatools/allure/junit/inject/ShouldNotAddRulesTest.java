package ru.yandex.qatools.allure.junit.inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.inject.Injector;
import ru.yandex.qatools.allure.junit.inject.testdata.*;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.09.13
 */
@RunWith(Parameterized.class)
public class ShouldNotAddRulesTest {

    private Class<?> origin;

    private Class<?> modify;

    public ShouldNotAddRulesTest(Class<?> origin) {
        this.origin = origin;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{EmptyClass.class},
                new Object[]{EmptyClassWithStaticConstructor.class},
                new Object[]{Interface.class},
                new Object[]{InterfaceWithTestMethod.class},
                new Object[]{AbstractClass.class},
                new Object[]{AbstractClassWithTestMethod.class},
                new Object[]{ClassWithPrivateTestMethod.class},
                new Object[]{TestClassWithTestCaseRule.class},
                new Object[]{TestClassWithTestSuiteRule.class}
        );
    }

    @Before
    public void injectAllureRules() throws Exception {
        modify = Injector.defineClass(origin.getName(), new JUnitRulesInjector().inject(origin));
    }

    @Test(expected = NoSuchFieldException.class)
    public void modifyClassDoesNotHaveTestCaseRuleFiledTest() throws Exception {
        modify.getField(JUnitRulesInjector.TEST_CASE_RULE_FIELD_NAME);
    }

    @Test(expected = NoSuchFieldException.class)
    public void modifyClassDoesNotHaveTestSuiteRuleFiledTest() throws Exception {
        modify.getField(JUnitRulesInjector.TEST_SUITE_RULE_FIELD_NAME);
    }
}
