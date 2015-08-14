package ru.yandex.qatools.allure.command;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class AllureCommandException extends Exception {

    public AllureCommandException(String message) {
        super(message);
    }

    public AllureCommandException(Throwable e) {
        super(e);
    }

    public AllureCommandException(String message, Throwable e) {
        super(message, e);
    }
}
