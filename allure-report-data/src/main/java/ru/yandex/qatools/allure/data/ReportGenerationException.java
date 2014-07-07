package ru.yandex.qatools.allure.data;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.10.13
 *         <p/>
 *         Signals that an attempt to generate the report in specified directory has failed.
 */
public class ReportGenerationException extends RuntimeException {

    /**
     * Constructs the {@link ru.yandex.qatools.allure.data.ReportGenerationException} from given cause
     *
     * @param cause given {@link java.lang.Throwable} cause
     */
    public ReportGenerationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs the {@link ru.yandex.qatools.allure.data.ReportGenerationException} with specified detail message
     *
     * @param message the detail message.
     */
    public ReportGenerationException(String message) {
        super(message);
    }
}
