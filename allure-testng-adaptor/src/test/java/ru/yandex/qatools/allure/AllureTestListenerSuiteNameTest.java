package ru.yandex.qatools.allure;

import org.junit.Test;
import org.testng.TestNG;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeThat;
import static ru.yandex.qatools.allure.AllureUtils.listTestSuiteXmlFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.02.15
 */
public class AllureTestListenerSuiteNameTest extends BasicListenerTest {

    @Override
    public void configure(TestNG testNG) {
        List<String> suites = new ArrayList<>();
        URL resource = getClass().getClassLoader().getResource("suite3.xml");
        assertNotNull("could not find suite3.xml", resource);

        //noinspection ConstantConditions
        suites.add(resource.getFile());
        testNG.setTestSuites(suites);
    }

    @Test
    public void shouldContainsBothSuitesWithDifferentNames() throws Exception {
        Collection<Path> files = listTestSuiteXmlFiles(resultsDirectory);
        assumeThat(files, hasSize(2));
        List<String> names = new ArrayList<>();
        for (Path file : files) {
            TestSuiteResult result = JAXB.unmarshal(file.toFile(), TestSuiteResult.class);
            names.add(result.getName());
        }

        assumeThat(names, containsInAnyOrder(
                "Test suite tag : Test tag by classes[param1=val1]",
                "Test suite tag : Test tag by classes 2[param1=val1]"
        ));
    }
}
