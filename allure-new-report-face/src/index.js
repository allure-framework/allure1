import App from './app';
import $ from 'jquery';
import Backbone from 'backbone';
import allurePlugins from './pluginApi';

import DescriptionView from './plugins/testcase/description/DescriptionView';

import OverviewLayout from './layouts/overview/OverivewLayout';
import DefectsLayout from './layouts/defects/DefectsLayout';

allurePlugins.addTab('', {
    title: 'Overview', icon: 'fa fa-home',
    route: '',
    onEnter: () => new OverviewLayout()
});
allurePlugins.addTab('defects', {
    title: 'Defects', icon: 'fa fa-flag',
    route: 'defects(/:defectId)(/:testcaseId)(/:attachmentId)',
    onEnter: (...routeParams) => new DefectsLayout({routeParams})
});
allurePlugins.addTab('xUnit', {
    title: 'xUnit', icon: 'fa fa-briefcase',
    route: 'xUnit(/:defectId)(/:testcaseId)(/:attachmentId)'
});
allurePlugins.addTab('behaviors', {
    title: 'Behaviors', icon: 'fa fa-list',
    route: 'behaviors(/:defectId)(/:testcaseId)(/:attachmentId)'
});
allurePlugins.addTab('graph', {
    title: 'Graph', icon: 'fa fa-bar-chart',
    route: 'graph'
});
allurePlugins.addTestcaseBlock(DescriptionView, {position: 'before'});

App.start();
Backbone.history.start();

window.jQuery = $;
