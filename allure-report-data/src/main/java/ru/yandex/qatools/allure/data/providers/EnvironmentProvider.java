package ru.yandex.qatools.allure.data.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.commons.model.Environment;
import ru.yandex.qatools.commons.model.Parameter;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listFilesByRegex;
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

        for (File file : listFilesByRegex(AllureConfig.newInstance().getEnvironmentXmlFileRegex(), inputDirectories)) {
            try {
                Environment environment = JAXB.unmarshal(file, Environment.class);
                merge(global, environment);
            } catch (Exception e) {
                logger.error("Can't unmarshal file " + file + " to environment bean.", e);
            }
        }

        for (File file : listFilesByRegex(AllureConfig.newInstance().getEnvironmentPropertiesFileRegex(), inputDirectories)) {
            try (FileInputStream fis = new FileInputStream(file)) {
                Properties properties = new Properties();
                properties.load(fis);
                merge(global, properties);
            } catch (Exception e) {
                logger.error("Can't unmarshal file " + file + " to environment bean.", e);
            }
        }

        return serialize(outputDirectory, ENVIRONMENT_JSON, global);
    }

    public void merge(Environment global, Environment environment) throws JAXBException {
        global.setId(environment.getId());
        global.setName(environment.getName());
        global.getParameter().addAll(environment.getParameter());
    }

    public void merge(Environment global, Properties properties) throws JAXBException {
        global.getParameter().addAll(convertToParameters(properties));
    }

    public Collection<Parameter> convertToParameters(Properties properties) {
        Collection<Parameter> parameters = new ArrayList<>();
        for (Object key : properties.keySet()) {
            Parameter parameter = new Parameter();
            parameter.setName(key.toString());
            parameter.setName(key.toString());
            parameter.setValue(properties.getProperty(key.toString()));
            parameters.add(parameter);
        }
        return parameters;
    }
}
