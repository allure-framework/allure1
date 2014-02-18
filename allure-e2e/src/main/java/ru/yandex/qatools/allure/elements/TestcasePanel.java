package ru.yandex.qatools.allure.elements;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Block;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Name("Testcase view")
@Block(@FindBy(css="[ui-view='testcase'] .pane_col"))
public class TestcasePanel extends HtmlElement {

    @Name("Testcase title")
    @FindBy(css=".pane__header h3")
    private HtmlElement title;

    public HtmlElement title() {
        return title;
    }
}
