class AllurePluginApi {
    tabs = [];

    addTab(tabName, {title, icon} = {}) {
        title = title || tabName;
        this.tabs.push({tabName, title, icon});
    }
}

export default new AllurePluginApi();
