import App from './app';
import $ from 'jquery';
import Backbone from 'backbone';
import allurePlugins from './pluginApi';
import './plugins/default';
import './plugins/xunit';
import './plugins/behaviors';

import DescriptionView from './plugins/testcase-description/DescriptionView';
import DefectsLayout from './plugins/tab-defects/DefectsLayout';

allurePlugins.addTab('defects', {
    title: 'Defects', icon: 'fa fa-flag',
    route: 'defects(/:defectId)(/:testcaseId)(/:attachmentId)',
    onEnter: (...routeParams) => new DefectsLayout({routeParams})
});
allurePlugins.addTab('graph', {
    title: 'Graph', icon: 'fa fa-bar-chart',
    route: 'graph'
});
allurePlugins.addTestcaseBlock(DescriptionView, {position: 'before'});

App.start();
Backbone.history.start();

window.jQuery = $;
