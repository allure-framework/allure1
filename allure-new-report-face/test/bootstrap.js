/*eslint-env node*/
const jsdom = require('jsdom').jsdom;
require('handlebars');
//require.cache[require.resolve('handlebars/runtime')] = {exports: Handlebars};

global.dump = require('debug')('allure-server:test');
global.document = jsdom('<html><head></head><body></body></html>', {
    url: 'http://localhost'
});
global.window = global.document.defaultView;
global.navigator = global.window.navigator;
global.location = global.window.location;
global.FormData = global.window.FormData;
global.XMLHttpRequest = global.window.XMLHttpRequest;

require.extensions['.css'] = function() {};

global.jQuery = require('jquery');
require('bem-components-dist/desktop/bem-components.dev.js+bh.js');

const MockAjax = require('jasmine-ajax');
jasmine.Ajax = new MockAjax(global);

require('babel-core/register');

