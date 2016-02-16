/*eslint-env commonjs*/
import i18next from 'i18next/lib';
import settings from './settings';

export default class I18NextBackend {
    static type = 'backend';

    init() {}

    read(language, namespace, callback) {
        callback(null, require('../translations/' + language + '.json'));
    }
}

export function init() {
    i18next.use(I18NextBackend);
    i18next.init({
        lng: settings.get('language'),
        fallbackLng: 'en'
    });
}

export default i18next;

