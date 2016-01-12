import App from './app';
import $ from 'jquery';
import Backbone from 'backbone';
import allurePlugins from './pluginApi';
import './plugins/default';
import './plugins/defects';
import './plugins/environment';
import './plugins/xunit';
import './plugins/behaviors';

import DescriptionView from './plugins/testcase-description/DescriptionView';

allurePlugins.addTab('graph', {
    title: 'Graph', icon: 'fa fa-bar-chart',
    route: 'graph'
});
allurePlugins.addTestcaseBlock(DescriptionView, {position: 'before'});

App.start();
Backbone.history.start();

window.jQuery = $;
