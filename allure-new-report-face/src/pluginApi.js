class AllurePluginApi {
    tabs = [];
    testcaseBlocks = {
        before: [],
        after: []
    };

    addTab(tabName, {title, icon} = {}) {
        title = title || tabName;
        this.tabs.push({tabName, title, icon});
    }

    addTestcaseBlock(view, {position}) {
        this.testcaseBlocks[position].push(view);
    }
}

export default new AllurePluginApi();
