package ru.yandex.qatools.allure.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;
import static ru.yandex.qatools.allure.config.AllureModelUtils.getAllureSchemaValidator;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.deleteFile;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformations;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */

public class TestRunGenerator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String SUITES_TO_TEST_RUN_XSL_1 = "xsl/suites-to-testrun-1.xsl";

    public static final String SUITES_TO_TEST_RUN_XSL_2 = "xsl/suites-to-testrun-2.xsl";

    public static final String SUITES_TO_TEST_RUN_XSL_3 = "xsl/suites-to-testrun-3.xsl";

    public static final String MIGRATION_1 = "xsl/migrations/13-14.move-severity-to-labels.xsl";

    private final ListFiles listFiles;

    private final boolean validateXML;

    public TestRunGenerator(boolean validateXML, File... dirs) {
        Collection<File> testSuitesFiles = listTestSuiteFiles(dirs);
        Collection<File> migrated = migrate(testSuitesFiles);
        this.listFiles = createListFiles(migrated);
        this.validateXML = validateXML;

    }

    private Collection<File> migrate(Collection<File> files) {
        Collection<File> result = new ArrayList<>();
        for (File file : files) {
            result.add(applyTransformations(file, MIGRATION_1));
        }
        return result;
    }

    private ListFiles createListFiles(Collection<File> files) {
        ListFiles lf = new ListFiles();
        for (File file : files) {
            try {
                if (validateXML) {
                    Validator validator = getAllureSchemaValidator();
                    validator.validate(new StreamSource(file));
                }
                lf.getFiles().add(file.toURI().toString());
            } catch (Exception e) {
                deleteFile(file);
                logger.error("File " + file + " skipped.", e);
            }
        }
        return lf;
    }

    public File generate() {
        File xml = null;

        try {
            xml = createListFiles();

            return applyTransformations(
                    xml,
                    SUITES_TO_TEST_RUN_XSL_1,
                    SUITES_TO_TEST_RUN_XSL_2,
                    SUITES_TO_TEST_RUN_XSL_3
            );
        } catch (Exception e) {
            throw new ReportGenerationException(e);
        } finally {
            deleteFile(xml);
        }
    }

    public File createListFiles() throws IOException {
        File xml = Files.createTempFile("list-files", ".xml").toFile();
        JAXB.marshal(new ObjectFactory().createListFiles(listFiles),
                new OutputStreamWriter(new FileOutputStream(xml), StandardCharsets.UTF_8)
        );
        return xml;
    }


    @SuppressWarnings("unused")
    public ListFiles getListFiles() {
        return listFiles;
    }
}
