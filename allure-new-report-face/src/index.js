import App from './app';
import $ from 'jquery';
import Backbone from 'backbone';
import allurePlugins from './pluginApi';
import './plugins/default';
import './plugins/defects';
import './plugins/environment';
import './plugins/xunit';
import './plugins/behaviors';
import './plugins/graph';

import DescriptionView from './plugins/testcase-description/DescriptionView';
allurePlugins.addTestcaseBlock(DescriptionView, {position: 'before'});

App.start();
Backbone.history.start();

window.jQuery = $;
