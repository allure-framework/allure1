package ru.yandex.qatools.allure.aspects;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.aspects.testdata.MySteps;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ru.yandex.qatools.allure.events.StepFailureEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.14
 */
public class AspectTest {

    private Allure allure = mock(Allure.class);

    private MySteps steps = new MySteps();

    @Before
    public void setUp() throws Exception {
        AllureStepsAspects.setAllure(allure);
        AllureAttachAspects.setAllure(allure);
    }

    @Test
    public void sampleStepTest() throws Exception {
        steps.sampleStep();
        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(eq(new StepStartedEvent("sampleStep")));
        inOrder.verify(allure).fire(eq(new StepFinishedEvent()));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void failedStepTest() throws Exception {
        Exception exception = new Exception("something");
        try {
            steps.failedStep(exception);
        } catch (Exception ignored) {
        }
        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(eq(new StepStartedEvent("failedStep[java.lang.Exception: something]")));
        inOrder.verify(allure).fire(eq(new StepFailureEvent().withThrowable(exception)));
        inOrder.verify(allure).fire(eq(new StepFinishedEvent()));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void stepWithTitleTest() throws Exception {
        steps.stepWithTitle();
        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(eq(new StepStartedEvent("stepWithTitle").withTitle("Tata title")));
        inOrder.verify(allure).fire(eq(new StepFinishedEvent()));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void staticStepTest() throws Exception {
        MySteps.sampleStaticStep();
        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(eq(new StepStartedEvent("sampleStaticStep")));
        inOrder.verify(allure).fire(eq(new StepFinishedEvent()));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void sampleByteAttachmentTest() throws Exception {
        byte[] bytes = "ata".getBytes(AllureConfig.newInstance().getAttachmentsEncoding());
        steps.byteAttachment(bytes);
        verify(allure).fire(eq(new MakeAttachmentEvent(bytes, "byteAttachment", "")));
        verifyNoMoreInteractions(allure);
    }

    @Test
    public void sampleStringAttachmentTest() throws Exception {
        byte[] bytes = "ata".getBytes(AllureConfig.newInstance().getAttachmentsEncoding());
        steps.stringAttachment("ata");
        verify(allure).fire(eq(new MakeAttachmentEvent(bytes, "super-title", "super-type")));
        verifyNoMoreInteractions(allure);
    }

    @Test
    public void sampleStringAttachmentWithParametersInTitleTest() throws Exception {
        byte[] bytes = "123".getBytes(AllureConfig.newInstance().getAttachmentsEncoding());
        steps.stringAttachment("message", "123");
        verify(allure).fire(eq(new MakeAttachmentEvent(bytes, "stringAttachment: message", "")));
        verifyNoMoreInteractions(allure);
    }
}
