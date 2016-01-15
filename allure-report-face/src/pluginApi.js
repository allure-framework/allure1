import router from './router';
import App from './app';

class AllurePluginApi {
    tabs = [];
    testcaseBlocks = {
        before: [],
        after: []
    };
    widgets = {};

    addTab(tabName, {title, icon, route, onEnter = App.tabNotFound} = {}) {
        title = title || tabName;
        this.tabs.push({tabName, title, icon});
        router.route(route, tabName);
        router.on('route:'+tabName, App.showView(onEnter));
    }

    addWidget(name, Widget) {
        this.widgets[name] = Widget;
    }

    addTestcaseBlock(view, {position}) {
        this.testcaseBlocks[position].push(view);
    }
}

export default new AllurePluginApi();
