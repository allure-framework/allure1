package ru.yandex.qatools.allure.elements;

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

    @Name("Features")
    @FindBy(css = "[ui-sref=\"features\"]")
    private Button behaviours;

    @Name("Graph")
    @FindBy(css = "[ui-sref=\"graph\"]")
    private Button graph;

    @Name("Timeline")
    @FindBy(css = "[ui-sref=\"timeline\"]")
    private Button timeline;

    public Button defects() {
        return defects;
    }

    public Button xUnit() {
        return xUnit;
    }

    public Button behaviours() {
        return behaviours;
    }

    public Button graph() {
        return graph;
    }

    public Button timeline() {
        return timeline;
    }
}
