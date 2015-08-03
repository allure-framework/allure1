package ru.yandex.qatools.allure.data.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.commons.model.Environment;
import ru.yandex.qatools.commons.model.Parameter;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listFilesByRegex;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
public class EnvironmentReader implements Reader<Environment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentReader.class);

    private Iterator<File> xmlIterator;

    private Iterator<File> propertiesIterator;

    @Inject
    public EnvironmentReader(@ResultDirectories File... inputDirectories) {
        AllureConfig config = AllureConfig.newInstance();
        xmlIterator = listFilesByRegex(
                config.getEnvironmentXmlFileRegex(),
                inputDirectories
        ).iterator();

        propertiesIterator = listFilesByRegex(
                config.getEnvironmentPropertiesFileRegex(),
                inputDirectories
        ).iterator();
    }

    @Override
    public Iterator<Environment> iterator() {
        return new EnvironmentIterator();
    }

    public static Collection<Parameter> convertToParameters(Properties properties) {
        Collection<Parameter> parameters = new ArrayList<>();
        for (Object key : properties.keySet()) {
            Parameter parameter = new Parameter();
            parameter.setKey(key.toString());
            parameter.setName(key.toString());
            parameter.setValue(properties.getProperty(key.toString()));
            parameters.add(parameter);
        }
        return parameters;
    }

    private class EnvironmentIterator implements Iterator<Environment> {
        @Override
        public boolean hasNext() {
            return xmlIterator.hasNext() || propertiesIterator.hasNext();
        }

        @Override
        public Environment next() {
            if (propertiesIterator.hasNext()) {
                File file = propertiesIterator.next();
                Properties properties = new Properties();
                try (FileInputStream fis = new FileInputStream(file)) {
                    properties.load(fis);
                    Environment result = new Environment();
                    result.getParameter().addAll(convertToParameters(properties));
                    return result;
                } catch (Exception e) {
                    LOGGER.error("Could not read environment .properties file " + file.getAbsolutePath(), e);
                    return next();
                }
            }
            if (xmlIterator.hasNext()) {
                File file = xmlIterator.next();
                try (FileInputStream fis = new FileInputStream(file)) {
                    return JAXB.unmarshal(fis, Environment.class);
                } catch (Exception e) {
                    LOGGER.error("Could not read environment .xml file " + file.getAbsolutePath(), e);
                    return next();
                }
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
