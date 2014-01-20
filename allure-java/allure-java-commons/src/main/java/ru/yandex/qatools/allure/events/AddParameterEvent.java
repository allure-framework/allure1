package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Parameter;
import ru.yandex.qatools.allure.model.ParameterKind;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
public class AddParameterEvent extends AbstractTestCaseAddParameterEvent {

    public AddParameterEvent(String name, String value) {
        setName(name);
        setValue(value);
    }

    @Override
    public void process(TestCaseResult context) {
        context.getParameters().add(new Parameter()
                .withName(getName())
                .withValue(getValue())
                .withKind(ParameterKind.ENVIRONMENT_VARIABLE)
        );
    }
}
