package ru.yandex.qatools.allure.blocks;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Block;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

@Block(@FindBy(css = ".tab-content > .xunit"))
public class XUnitTab extends HtmlElement {

    @Name("Testsuites list")
    @FindBy(css=".pane_col:first-child [allure-table] tr")
    private List<Button> testsuites;

    @Name("Testsuite view")
    @FindBy(css=".pane_col:nth-child(2)")
    private HtmlElement testsuite;


    @Name("Testsuite title")
    @FindBy(css=".pane_col:nth-child(2) .pane__header h3")
    private HtmlElement testsuiteTitle;

    public Button testsuiteAt(int index) {
        return testsuites.get(index);
    }

    public HtmlElement testsuite() {
        return testsuite;
    }

    public HtmlElement testsuiteTitle() {
        return testsuiteTitle;
    }

}
