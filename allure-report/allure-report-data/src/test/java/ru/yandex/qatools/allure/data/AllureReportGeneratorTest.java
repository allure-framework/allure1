package ru.yandex.qatools.allure.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.listFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
public class AllureReportGeneratorTest {

    private final static String JSON_FILE = ".+\\.json";

    private final static String ANY_FILE = ".+";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File origin;

    private File output;

    @Before
    public void setOriginDirectory() throws Exception {
        origin = new File(ClassLoader.getSystemResource("origin").getFile());
    }

    @Before
    public void generate() throws Exception {
        File input = new File(ClassLoader.getSystemResource("testdata3").getFile());
        output = folder.newFolder();

        AllureReportGenerator reportGenerator = new AllureReportGenerator(input);
        reportGenerator.generate(output);
    }

    @Test
    public void checkJSONFiles() throws Exception {
        File[] originFiles = listFiles(origin, JSON_FILE);

        for (File file : originFiles) {
            String checkedFileName = file.getName();

            assertTrue(
                    String.format("Result directory don't contains \"%s\" file", checkedFileName),
                    contains(output, checkedFileName)
            );

            File actual = getFile(output, checkedFileName);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode originNode = objectMapper.readTree(file);
            JsonNode resultNode = objectMapper.readTree(actual);

            assertEquals(String.format("Changes in file \"%s\"", checkedFileName), originNode, resultNode);
        }

    }

    @Test
    public void filesCountTest() throws Exception {
        File[] originFiles = listFiles(origin, ANY_FILE);
        File[] actualFiles = listFiles(output, ANY_FILE);

        assertEquals(originFiles.length, actualFiles.length);
    }

    private boolean contains(File directory, String fileName) {
        return listFiles(directory, fileName).length > 0;
    }

    private File getFile(File directory, String fileName) {
        return listFiles(directory, fileName)[0];
    }


}
