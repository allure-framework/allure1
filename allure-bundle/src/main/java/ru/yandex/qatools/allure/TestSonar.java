package ru.yandex.qatools.allure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 03.08.15
 */
public class TestSonar {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void doSomething() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("my").append(" text ").append(" for ").append(" sonar ").append(" github ").append(" integration ").append(" test ");
        logger.info(stringBuffer.toString());
    }
}
