package ru.yandex.qatools.allure.data.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.commons.model.Environment;
import ru.yandex.qatools.commons.model.Parameter;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import static ru.yandex.qatools.allure.AllureUtils.listFiles;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
public class EnvironmentReader implements Reader<Environment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentReader.class);

    private Iterator<Path> xmlIterator;

    private Iterator<Path> propertiesIterator;

    @Inject
    public EnvironmentReader(@ResultDirectories Path... inputDirectories) throws IOException {
        xmlIterator = listFiles(
                "*-environment.xml",
                inputDirectories
        ).iterator();

        propertiesIterator = listFiles(
                "*-environment.properties",
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
                Path file = propertiesIterator.next();
                Properties properties = new Properties();
                try (InputStream is = Files.newInputStream(file)) {
                    properties.load(is);
                    Environment result = new Environment();
                    result.getParameter().addAll(convertToParameters(properties));
                    return result;
                } catch (Exception e) {
                    LOGGER.error("Could not read environment .properties file " +
                            file.toAbsolutePath().toString(), e);
                    return next();
                }
            }
            if (xmlIterator.hasNext()) {
                Path file = xmlIterator.next();
                try (InputStream is = Files.newInputStream(file)) {
                    return JAXB.unmarshal(is, Environment.class);
                } catch (Exception e) {
                    LOGGER.error("Could not read environment .xml file " +
                            file.toAbsolutePath().toString(), e);
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
