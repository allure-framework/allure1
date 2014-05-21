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
    @FindBy(css=".pane_content [allure-table] tbody tr")
    private List<Button> defects;

    @Name("Defect view")
    @FindBy(css=".pane_col:nth-child(2)")
    private HtmlElement defect;

    public Button defectAt(int index) {
        return defects.get(index);
    }

    public HtmlElement currentDefect() {
        return defect;
    }
}
