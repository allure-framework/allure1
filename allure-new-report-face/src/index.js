import App from './app';
import $ from 'jquery';
import Backbone from 'backbone';
import allurePlugins from './pluginApi';

import DescriptionView from './plugins/testcase/description/DescriptionView';

allurePlugins.addTab('', {title: 'Overview', icon: 'fa fa-home'});
allurePlugins.addTab('behaviors', {title: 'Behaviors', icon: 'fa fa-list'});
allurePlugins.addTab('defects', {title: 'Defects', icon: 'fa fa-flag'});
allurePlugins.addTab('graph', {title: 'Graph', icon: 'fa fa-bar-chart'});
allurePlugins.addTestcaseBlock(DescriptionView, {position: 'before'});

App.start();
Backbone.history.start();

window.jQuery = $;
