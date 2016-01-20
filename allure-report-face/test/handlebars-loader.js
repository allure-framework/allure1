/*eslint-env node*/
const fs = require('fs');
const path = require('path');
const Handlebars = require('handlebars/dist/cjs/handlebars');
require.cache[require.resolve('handlebars/runtime')] = {exports: Handlebars};
Handlebars.registerHelper('helperMissing', function() {
    if(arguments.length === 1) {
        return;
    }
    const options = Array.from(arguments).pop();
    const helper = require('helpers/' + options.name);
    return helper.apply(this, arguments);
});
const resolvePartial = Handlebars.VM.resolvePartial;

require.extensions['.hbs'] = function(module, filename) {
    Handlebars.VM.resolvePartial = function(partial, content, options) {
        if(!partial) {
            partial = require(path.resolve(path.dirname(filename), options.name) + '.hbs');
        }
        return resolvePartial.call(this, partial, content, options);
    };
    var templateString = fs.readFileSync(filename, 'utf8');
    module.exports = Handlebars.compile(templateString);
};
