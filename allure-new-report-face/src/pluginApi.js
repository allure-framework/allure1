import router from './router';
import App from './app';

class AllurePluginApi {
    tabs = [];
    testcaseBlocks = {
        before: [],
        after: []
    };

    addTab(tabName, {title, icon, route, onEnter = App.tabNotFound} = {}) {
        title = title || tabName;
        this.tabs.push({tabName, title, icon});
        router.route(route, tabName);
        router.on('route:'+tabName, App.showView(onEnter));
    }

    addTestcaseBlock(view, {position}) {
        this.testcaseBlocks[position].push(view);
    }
}

export default new AllurePluginApi();
