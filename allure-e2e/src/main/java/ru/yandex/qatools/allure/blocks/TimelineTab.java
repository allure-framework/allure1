package ru.yandex.qatools.allure.blocks;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Block;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

@Block(@FindBy(css = ".tab-content > .timeline"))
public class TimelineTab extends HtmlElement {

    @Name("Bars")
    @FindBy(css=".bar")
    private List<HtmlElement> bars;

    @Name("Testcase")
    @FindBy(css="[ui-view=\"testcase\"] .pane_col")
    private HtmlElement testcase;

    public HtmlElement barAt(int index) {
        return bars.get(index);
    }

    public HtmlElement testcasePane() {
        return testcase;
    }
}
