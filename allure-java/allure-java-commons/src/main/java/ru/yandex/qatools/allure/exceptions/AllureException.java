package ru.yandex.qatools.allure.exceptions;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 */

public class AllureException extends RuntimeException {
    public AllureException(String message) {
        super(message);
    }

    public AllureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllureException(Throwable cause) {
        super(cause);
    }
}
