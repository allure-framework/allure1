package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.ReportGenerationException;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.deleteFile;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformations;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.10.14
 */
public abstract class AbstractDataProvider implements DataProvider {

    @Override
    public long provide(File testPack, File[] inputDirectories, File outputDirectory) {
        File body = null;
        try {
            body = applyTransformations(
                    testPack,
                    getXslTransformations()
            );

            return serialize(outputDirectory, body);
        } catch (Exception e) {
            throw new ReportGenerationException(e);
        } finally {
            deleteFile(body);
        }
    }

    @Override
    public DataProviderPhase getPhase() {
        return DataProviderPhase.DEFAULT;
    }

    protected long serialize(File outputDirectory, File body) throws IOException {
        try (Reader reader = new FileReader(body)) {
            return serialize(outputDirectory, getType(), getJsonFileName(), reader);
        }
    }

    protected <T> long serialize(File outputDirectory, Class<T> type, String name, Reader reader) {
        T result = JAXB.unmarshal(
                reader,
                type
        );
        return AllureReportUtils.serialize(outputDirectory, name, result);
    }

    public abstract String[] getXslTransformations();

    public abstract String getJsonFileName();

    public abstract Class<?> getType();
}
