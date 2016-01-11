import App from './app';
import $ from 'jquery';
import Backbone from 'backbone';
import allurePlugins from './pluginApi';

import DescriptionView from './plugins/testcase-description/DescriptionView';
import DefectsLayout from './plugins/tab-defects/DefectsLayout';
import XUnitLayout from './plugins/tab-xunit/XUnitLayout';
import BehaviorsLayout from './plugins/tab-behaviors/BehaviorsLayout';

import OverviewLayout from './layouts/overview/OverivewLayout';

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
    route: 'xUnit(/:defectId)(/:testcaseId)(/:attachmentId)',
    onEnter: (...routeParams) => new XUnitLayout({routeParams})
});
allurePlugins.addTab('behaviors', {
    title: 'Behaviors', icon: 'fa fa-list',
    route: 'behaviors(/:defectId)(/:testcaseId)(/:attachmentId)',
    onEnter: (...routeParams) => new BehaviorsLayout({routeParams})
});
allurePlugins.addTab('graph', {
    title: 'Graph', icon: 'fa fa-bar-chart',
    route: 'graph'
});
allurePlugins.addTestcaseBlock(DescriptionView, {position: 'before'});

App.start();
Backbone.history.start();

window.jQuery = $;
