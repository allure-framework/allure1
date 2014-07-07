package ru.yandex.qatools.allure.data.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.commons.model.Environment;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listFilesByName;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformations;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.07.14
 */
public class EnvironmentProvider implements DataProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String TESTRUN_TO_ENVIRONMENT_XSL = "xsl/testrun-to-environment.xsl";

    public static final String ENVIRONMENT_JSON = "environment.json";

    @Override
    public long provide(String testPack, File[] inputDirectories, File outputDirectory) {
        String allureEnvironmentBody = applyTransformations(
                testPack,
                TESTRUN_TO_ENVIRONMENT_XSL
        );

        Environment global = JAXB.unmarshal(
                new StringReader(allureEnvironmentBody),
                Environment.class
        );

        for (File file : listFilesByName(AllureConfig.newInstance().getEnvironmentFileName(), inputDirectories)) {
            try {
                merge(global, file);
            } catch (Exception e) {
                logger.error("Can't unmarshal file " + file + " to environment bean.");
            }
        }

        return serialize(outputDirectory, ENVIRONMENT_JSON, global);
    }

    public void merge(Environment global, File file) throws JAXBException {
        Environment environment = JAXB.unmarshal(file, Environment.class);

        global.setId(environment.getId());
        global.setName(environment.getName());
        global.getParameter().addAll(environment.getParameter());
    }
}
