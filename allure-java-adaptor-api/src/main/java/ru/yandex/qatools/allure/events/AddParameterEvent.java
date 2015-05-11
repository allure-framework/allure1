package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Parameter;
import ru.yandex.qatools.allure.model.ParameterKind;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * Using to add parameters to TestCase. Parameters will be shown
 * at Allure report.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 *         <p/>
 * @see ru.yandex.qatools.allure.events.TestCaseEvent
 */
public class AddParameterEvent extends AbstractTestCaseAddParameterEvent {

    /**
     * Constructs an new event with specified name and value
     *
     * @param name  of parameter to add
     * @param value of parameter to add
     */
    public AddParameterEvent(String name, String value) {
        this(name, value, ParameterKind.ENVIRONMENT_VARIABLE);
    }

    /**
     * Constructs an new event with specified name and value
     *
     * @param name  of parameter to add
     * @param value of parameter to add
     * @param kind of parameter to add
     */
    public AddParameterEvent(String name, String value, ParameterKind kind) {
        setName(name);
        setValue(value);
        setKind(kind.name());
    }

    /**
     * Add parameter to testCase
     *
     * @param context which can be changed
     */
    @Override
    public void process(TestCaseResult context) {
        context.getParameters().add(new Parameter()
                        .withName(getName())
                        .withValue(getValue())
                        .withKind(ParameterKind.valueOf(getKind()))
        );
    }
}
