import 'font-awesome/css/font-awesome.css';
import './styles.css';

import 'bem-components-dist/desktop/bem-components.dev.js+bh.js';
import {Application} from 'backbone.marionette';
import router from './router';
import ErrorLayout from './layouts/error/ErrorLayout';

function rootPath(path) {
    return path.split('/')[0];
}

const App = new Application({
    regions: {
        'error': '#alert',
        'content': '#content',
        'popup': '#popup'
    }
});

App.showView = (factory) => {
    var view;
    return (...args) => {
        if(view && rootPath(router.getCurrentUrl()) === rootPath(router.lastUrl)) {
            view.onRouteUpdate(...args);
        } else {
            view = factory(...args);
            App.getRegion('content').show(view);
        }
    };
};

App.tabNotFound = App.showView(() =>
    new ErrorLayout({
        code: 404,
        message: 'Not Found'
    })
);

App.on('start', () => {
    router.on('route:notFound', App.tabNotFound);
});

export default App;
