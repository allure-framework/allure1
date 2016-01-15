import {SafeString} from 'handlebars/runtime';

const URL_REGEXP = /^(\w)+:\/\/.*/;

export default function(text) {
    return URL_REGEXP.test(text) ? new SafeString(`<a href="${text}"  class="link link_theme_islands" target="_blank">${text}</a>`) : text;
}
