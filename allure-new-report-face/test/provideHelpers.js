/*eslint-env node*/
import Handlebars from 'handlebars';

export default function(...helpers) {
    helpers.forEach((helperName) => {
        var helperFn;
        try {
            helperFn = require('../src/helpers/' + helperName);
        } catch (e) {
            helperFn = require('../src/blocks/' + helperName);
        }
        Handlebars.registerHelper(helperName, helperFn);
    });
}
