allureCore.addTab('example-tab', {
    title: 'Example',
    icon: 'fa fa-clock-o',
    onRender(element) {
        this.view = new TableView({
            columns: [],
            lang: allureCore.options.language,
            el: element
        }).render();
    },
    onDestroy(element) {
        this.view.destroy();
    }
});
