package ru.yandex.qatools.allure.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 27.03.14
 */
@RunWith(Parameterized.class)
public class XmlEscapeHandlerTest {

    private File testSuiteResultFile;

    private Marshaller m;

    private TestSuiteResult result;

    private String character;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public XmlEscapeHandlerTest(String character) {
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
        m = JAXBContext.newInstance(TestSuiteResult.class).createMarshaller();
        m.setProperty(
                CharacterEscapeHandler.class.getName(),
                XmlEscapeHandler.getInstance()
        );

        result = new TestSuiteResult().withName("name");
    }

    @Test
    public void dataWithInvalidCharacterTest() throws Exception {
        result.setTitle(character);
        m.marshal(new ObjectFactory().createTestSuite(result), testSuiteResultFile);
        Validator validator = AllureModelUtils.getAllureSchemaValidator();
        validator.validate(new StreamSource(testSuiteResultFile));
    }
}
