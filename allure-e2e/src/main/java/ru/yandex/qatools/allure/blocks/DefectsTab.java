package ru.yandex.qatools.allure.blocks;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Block;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

@Block(@FindBy(css = ".tab-content > .defects"))
public class DefectsTab extends HtmlElement {

    @Name("Defects list")
    @FindBy(css=".pane_content .defect_item .list-item")
    private List<Button> defects;

    @Name("Testcase view")
    @FindBy(css="[ui-view='testcase'] .pane_col")
    private HtmlElement testcase;


    @Name("Testcase title")
    @FindBy(css="[ui-view='testcase'] .pane_col .pane__header h3")
    private HtmlElement testcaseTitle;

    public Button defectAt(int index) {
        return defects.get(index);
    }

    public HtmlElement testcase() {
        return testcase;
    }

    public HtmlElement testcaseTitle() {
        return testcaseTitle;
    }
}
