package ru.yandex.qatools.allure;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.blocks.DefectsTab;
import ru.yandex.qatools.allure.blocks.TimelineTab;
import ru.yandex.qatools.allure.blocks.XUnitTab;
import ru.yandex.qatools.allure.elements.Tabs;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public class Page {

    @Name("Tabs switcher")
    private Tabs tabs;

    @Name("xUnit Tab content")
    private XUnitTab xUnitTab;

    @Name("Defects Tab content")
    private DefectsTab defectsTab;

    @Name("Timeline Tab content")
    private TimelineTab timelineTab;

    @Name("Common tabs wrapper")
    @FindBy(css = ".tab-content")
    private HtmlElement tabContent;

    public Page(WebDriver driver) {
        HtmlElementLoader.populatePageObject(this, driver);
    }

    public Tabs tabs() {
        return tabs;
    }

    public HtmlElement tabContent() {
        return tabContent;
    }

    public XUnitTab xUnitTabContent() {
        return xUnitTab;
    }

    public DefectsTab defectsTabContent() {
        return defectsTab;
    }

    public TimelineTab timelineTab() {
        return timelineTab;
    }
}
