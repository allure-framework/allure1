package ru.yandex.qatools.allure;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Block;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Block(@FindBy(css = ".b-vert"))
public class Tabs extends HtmlElement {

    @Name("Defects")
    @FindBy(css = "[ui-sref=\"defects\"]")
    private Button defects;

    @Name("XUnit")
    @FindBy(css = "[ui-sref=\"home\"]")
    private Button xUnit;

    public Button xUnit() {
        return xUnit;
    }

    public Button defects() {
        return defects;
    }
}