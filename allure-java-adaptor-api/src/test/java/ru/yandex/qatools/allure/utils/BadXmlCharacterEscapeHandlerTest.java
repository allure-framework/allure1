package ru.yandex.qatools.allure.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 27.03.14
 */
@RunWith(Parameterized.class)
public class BadXmlCharacterEscapeHandlerTest {

    private File testSuiteResultFile;

    private TestSuiteResult result;

    private String character;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public BadXmlCharacterEscapeHandlerTest(String character) {
        this.character = character;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{"\u0000"},
                new Object[]{"\u0017"}
        );
    }

    @Before
    public void setUp() throws Exception {
        testSuiteResultFile = folder.newFile();
        result = new TestSuiteResult().withName("name-and-кириллицей-also");
        result.setTitle("prefix " + character + " suffix");
    }

    @Test
    public void dataWithInvalidCharacterTest() throws Exception {
        AllureResultsUtils.writeTestSuiteResult(result, testSuiteResultFile);

        Validator validator = AllureModelUtils.getAllureSchemaValidator();
        validator.validate(new StreamSource(testSuiteResultFile));

        TestSuiteResult testSuite = JAXB.unmarshal(testSuiteResultFile, TestSuiteResult.class);
        assertThat(testSuite.getName(), is("name-and-кириллицей-also"));
        assertTrue(testSuite.getTitle().startsWith("prefix "));
        assertTrue(testSuite.getTitle().endsWith(" suffix"));
    }
}
