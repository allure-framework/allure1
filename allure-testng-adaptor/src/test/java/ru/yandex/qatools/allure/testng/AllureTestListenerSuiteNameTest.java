package ru.yandex.qatools.allure.testng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.testng.TestNG;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeThat;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.02.15
 */
public class AllureTestListenerSuiteNameTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();


    private static File resultsDir;

    @Before
    public void setUp() throws IOException {
        resultsDir = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDir);

        List<String> suites = new ArrayList<>();
        URL resource = getClass().getClassLoader().getResource("suite3.xml");
        assertNotNull("could not find suite3.xml", resource);

        //noinspection ConstantConditions
        suites.add(resource.getFile());

        TestNG testNG = new TestNG();
        testNG.setTestSuites(suites);
        testNG.setUseDefaultListeners(false);
        testNG.run();
    }

    @Test
    public void shouldContainsBothSuitesWithDifferentNames() throws Exception {
        Collection<File> files = listTestSuiteFiles(resultsDir);
        assumeThat(files, hasSize(2));
        List<String> names = new ArrayList<>();
        for (File file : files) {
            TestSuiteResult result = JAXB.unmarshal(file, TestSuiteResult.class);
            names.add(result.getName());
        }

        assumeThat(names, containsInAnyOrder(
                "Test suite tag : Test tag by classes[param1=val1]",
                "Test suite tag : Test tag by classes 2[param1=val1]"
        ));
    }
}
