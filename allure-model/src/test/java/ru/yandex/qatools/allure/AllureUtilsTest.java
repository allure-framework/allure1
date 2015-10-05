package ru.yandex.qatools.allure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.LabelName;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static ru.yandex.qatools.allure.AllureUtils.generateTestSuiteName;
import static ru.yandex.qatools.allure.AllureUtils.marshalTestSuite;
import static ru.yandex.qatools.allure.AllureUtils.validateResults;
import static ru.yandex.qatools.allure.Matchers.exists;
import static ru.yandex.qatools.allure.Matchers.notExists;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.05.14
 */
public class AllureUtilsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public Path resultsDirectory;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder().toPath();
    }

    @Test(expected = IllegalStateException.class)
    public void createInstanceTest() throws Exception {
        new AllureUtils();
    }

    @Test
    public void shouldEscapeBadCharactersWhileMarshal() throws Exception {
        Path suite = resultsDirectory.resolve(generateTestSuiteName());
        marshalTestSuite(new TestSuiteResult().withName("hi\uFFFEme"), suite);
        validateResults(resultsDirectory);
        TestSuiteResult result = JAXB.unmarshal(suite.toFile(), TestSuiteResult.class);
        assertThat(result.getName(), is("hi me"));
    }

    @Test
    public void shouldCloseTestSuiteFile() throws Exception {
        Path suite = resultsDirectory.resolve(generateTestSuiteName());
        marshalTestSuite(new TestSuiteResult(), suite);

        assertThat(suite, exists());

        Files.delete(suite);
        assertThat(suite, notExists());
    }

    @Test
    public void createFeatureLabelTest() throws Exception {
        Label label = AllureUtils.createFeatureLabel("some-feature");
        assertThat(label.getValue(), is("some-feature"));
        assertThat(label.getName(), is(LabelName.FEATURE.value()));
    }

    @Test
    public void createStoryLabelTest() throws Exception {
        Label label = AllureUtils.createStoryLabel("some-story");
        assertThat(label.getValue(), is("some-story"));
        assertThat(label.getName(), is(LabelName.STORY.value()));
    }

    @Test
    public void createSeverityLabelTest() throws Exception {
        Label label = AllureUtils.createSeverityLabel(SeverityLevel.BLOCKER);
        assertThat(label.getValue(), is(SeverityLevel.BLOCKER.value()));
        assertThat(label.getName(), is(LabelName.SEVERITY.value()));
    }

    @Test
    public void createTestLabelTest() {
        Label label = AllureUtils.createTestLabel("some-test");
        assertThat(label.getValue(), is("some-test"));
        assertThat(label.getName(), is(LabelName.TEST_ID.value()));
    }

    @Test
    public void shouldCreateProgrammingLabel() throws Exception {
        Label label = AllureUtils.createProgrammingLanguageLabel();
        assertThat(label.getName(), is(LabelName.LANGUAGE.value()));
        assertThat(label.getValue(), is("JAVA"));
    }

    @Test
    public void shouldCreateFrameworkLabel() throws Exception {
        Label label = AllureUtils.createTestFrameworkLabel("framework");
        assertThat(label.getName(), is(LabelName.FRAMEWORK.value()));
        assertThat(label.getValue(), is("framework"));
    }

    @Test
    public void shouldCreateIssueLabel() throws Exception {
        Label label = AllureUtils.createIssueLabel("issue");
        assertThat(label.getName(), is(LabelName.ISSUE.value()));
        assertThat(label.getValue(), is("issue"));
    }

    @Test
    public void shouldCreateHostLabel() throws Exception {
        Label label = AllureUtils.createHostLabel("host");
        assertThat(label.getName(), is(LabelName.HOST.value()));
        assertThat(label.getValue(), is("host"));
    }

    @Test
    public void shouldCreateThreadLabel() throws Exception {
        Label label = AllureUtils.createThreadLabel("thread");
        assertThat(label.getName(), is(LabelName.THREAD.value()));
        assertThat(label.getValue(), is("thread"));
    }
}
