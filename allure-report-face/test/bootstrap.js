/* eslint-env node */

//add extra modules root
process.env.NODE_PATH = 'src';
module.constructor._initPaths();

const jsdom = require('jsdom').jsdom;
const Handlebars = require('handlebars');
require.cache[require.resolve('handlebars/runtime')] = {exports: Handlebars};
Handlebars.registerHelper('helperMissing', function() {
    const options = Array.from(arguments).pop();
    const helper = require('helpers/' + options.name);
    return helper.apply(this, arguments);
});

//jasmine addons
global.joc = jasmine.objectContaining;
global.jany = jasmine.any;

//debug log
global.dump = require('debug')('allure-face:test');

//jsdom
global.document = jsdom('<html><head></head><body></body></html>', {
    url: 'http://localhost'
});
global.window = global.document.defaultView;
global.window.localStorage = jasmine.createSpyObj('localStorage', ['getItem', 'setItem']);
global.navigator = global.window.navigator;
global.location = global.window.location;

//require hooks
require.extensions['.css'] = function() {};
require('babel-core/register');
