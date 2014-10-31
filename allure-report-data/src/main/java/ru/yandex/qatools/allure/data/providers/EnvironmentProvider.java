package ru.yandex.qatools.allure.data.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;
import ru.yandex.qatools.commons.model.Environment;
import ru.yandex.qatools.commons.model.Parameter;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static java.lang.String.format;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listFilesByRegex;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.07.14
 */
public class EnvironmentProvider extends AbstractDataProvider {

    public static final AllureConfig ALLURE_CONFIG = AllureConfig.newInstance();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String TESTRUN_TO_ENVIRONMENT_XSL = "xsl/testrun-to-environment.xsl";

    public static final String ENVIRONMENT_JSON = "environment.json";

    public Collection<File> environmentXml = new ArrayList<>();

    public Collection<File> environmentProperties = new ArrayList<>();

    @Override
    public long provide(File testPack, File[] inputDirectories, File outputDirectory) {
        environmentXml = listFilesByRegex(ALLURE_CONFIG.getEnvironmentXmlFileRegex(), inputDirectories);
        environmentProperties = listFilesByRegex(ALLURE_CONFIG.getEnvironmentPropertiesFileRegex(), inputDirectories);
        return super.provide(testPack, inputDirectories, outputDirectory);
    }

    @Override
    protected <T> long serialize(File outputDirectory, Class<T> type, String name, Reader reader) {
        Environment global = JAXB.unmarshal(
                reader,
                Environment.class
        );

        for (File file : environmentXml) {
            try {
                Environment environment = JAXB.unmarshal(file, Environment.class);
                merge(global, environment);
            } catch (Exception e) {
                logger.error(format("Can't unmarshal environment file %s to environment bean.", file), e);
            }
        }

        for (File file : environmentProperties) {
            try (FileInputStream fis = new FileInputStream(file)) {
                Properties properties = new Properties();
                properties.load(fis);
                merge(global, properties);
            } catch (Exception e) {
                logger.error(format("Can't read properties file %s to environment bean.", file), e);
            }
        }

        return AllureReportUtils.serialize(outputDirectory, ENVIRONMENT_JSON, global);
    }

    @Override
    public String[] getXslTransformations() {
        return new String[]{TESTRUN_TO_ENVIRONMENT_XSL};
    }

    @Override
    public String getJsonFileName() {
        return null;
    }

    @Override
    public Class<?> getType() {
        return null;
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
