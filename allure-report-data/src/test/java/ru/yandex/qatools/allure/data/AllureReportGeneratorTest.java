package ru.yandex.qatools.allure.data;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Providers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ReportWriter;
import ru.yandex.qatools.allure.data.plugins.PluginManager;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.commons.model.Environment;

import java.io.File;
import java.util.Arrays;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listAttachmentFiles;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listFilesByRegex;
import static ru.yandex.qatools.allure.data.utils.DirectoryMatcher.contains;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.02.15
 */
public class AllureReportGeneratorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldGenerate() throws Exception {

        TestCaseResult result = new TestCaseResult().withName("result");
        @SuppressWarnings("unchecked")
        Reader<TestCaseResult> testCaseResultReader = mock(Reader.class);
        doReturn(Arrays.asList(result).iterator()).when(testCaseResultReader).iterator();

        AllureTestCase testCase = new AllureTestCase();
        testCase.setUid("uid");
        testCase.setName("name");

        TestCaseConverter converter = mock(TestCaseConverter.class);
        doReturn(testCase).when(converter).convert(eq(result));

        Environment environment = new Environment().withId("envId");
        @SuppressWarnings("unchecked")
        Reader<Environment> environmentReader = mock(Reader.class);
        doReturn(Arrays.asList(environment).iterator()).when(environmentReader).iterator();

        AttachmentInfo attachment = new AttachmentInfo();
        attachment.setName("name");
        attachment.setPath("path");
        @SuppressWarnings("unchecked")
        Reader<AttachmentInfo> attachmentReader = mock(Reader.class);
        doReturn(Arrays.asList(attachment).iterator()).when(attachmentReader).iterator();

        PluginManager pluginManager = mock(PluginManager.class);
        ReportWriter writer = mock(ReportWriter.class);

        AllureReportGenerator generator = new AllureReportGenerator(new TestInjector(
                testCaseResultReader, environmentReader, attachmentReader, converter, pluginManager
        ));

        generator.generate(writer);

        verify(converter).convert(result);
        verifyNoMoreInteractions(converter);

        InOrder inOrder = inOrder(pluginManager);
        inOrder.verify(pluginManager).prepare(result);
        inOrder.verify(pluginManager).prepare(testCase);
        inOrder.verify(pluginManager).process(testCase);
        inOrder.verify(pluginManager).writePluginData(AllureTestCase.class, writer);
        inOrder.verify(pluginManager).prepare(environment);
        inOrder.verify(pluginManager).process(environment);
        inOrder.verify(pluginManager).writePluginData(Environment.class, writer);
        inOrder.verify(pluginManager).prepare(attachment);
        inOrder.verify(pluginManager).writePluginResources(writer);
        inOrder.verify(pluginManager).writePluginList(writer);
        inOrder.verify(pluginManager).writePluginWidgets(writer);

        verifyNoMoreInteractions(pluginManager);

        verify(writer).write(testCase);
        verify(writer).write(attachment);
        verify(writer).writeReportInfo();
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void shouldGenerateWithoutFailures() throws Exception {
        AllureReportGenerator generator = new AllureReportGenerator(new File("target/allure-results"));
        File outputDirectory = folder.newFolder();
        File[] listBefore = outputDirectory.listFiles();
        assumeTrue("Output directory must be empty ", listBefore != null && listBefore.length == 0);
        generator.generate(outputDirectory);

        File dataDirectory = new File(outputDirectory, ReportWriter.DATA_DIRECTORY_NAME);
        assertTrue("Data directory should be created", dataDirectory.exists());

        assertThat(dataDirectory, contains("xunit.json"));
        assertThat(dataDirectory, contains("timeline.json"));
        assertThat(dataDirectory, contains("behaviors.json"));
        assertThat(dataDirectory, contains("defects.json"));
        assertThat(dataDirectory, contains("environment.json"));
        assertThat(dataDirectory, contains("graph.json"));
        assertThat(dataDirectory, contains(PluginManager.WIDGETS_JSON));
        assertThat(dataDirectory, contains(ReportWriter.REPORT_JSON));

        assertThat(listAttachmentFiles(dataDirectory), not(empty()));
        assertThat(listFilesByRegex(".+-testcase\\.json", dataDirectory), not(empty()));
    }

    class TestInjector extends AbstractModule {

        TestCaseConverter converter;

        Reader<TestCaseResult> testCaseReader;

        Reader<Environment> environmentReader;

        Reader<AttachmentInfo> attachmentReader;

        PluginManager pluginManager;

        public TestInjector(Reader<TestCaseResult> testCaseReader, Reader<Environment> environmentReader,
                            Reader<AttachmentInfo> attachmentReader, TestCaseConverter converter,
                            PluginManager pluginManager) {
            this.converter = converter;
            this.testCaseReader = testCaseReader;
            this.attachmentReader = attachmentReader;
            this.environmentReader = environmentReader;
            this.pluginManager = pluginManager;
        }

        @Override
        protected void configure() {
            bind(new TypeLiteral<Reader<TestCaseResult>>() {
            }).toProvider(Providers.of(testCaseReader));
            bind(new TypeLiteral<Reader<Environment>>() {
            }).toProvider(Providers.of(environmentReader));
            bind(new TypeLiteral<Reader<AttachmentInfo>>() {
            }).toProvider(Providers.of(attachmentReader));
            bind(new TypeLiteral<TestCaseConverter>() {
            }).toProvider(Providers.of(converter));
            bind(PluginManager.class).toProvider(Providers.of(pluginManager));
        }
    }
}
