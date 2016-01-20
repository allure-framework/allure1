import 'font-awesome/css/font-awesome.css';
import './styles.css';
import './blocks/table/styles.css';

import {Application, Behaviors} from 'backbone.marionette';
import router from './router';
import * as behaviors from './behaviors';
import ErrorLayout from './layouts/error/ErrorLayout';

function rootPath(path) {
    return path.split('/')[0];
}

Behaviors.behaviorsLookup = behaviors;

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

App.tabNotFound = () => new ErrorLayout({
    code: 404,
    message: 'Not Found'
});

App.on('start', () => {
    router.on('route:notFound', App.showView(App.tabNotFound));
});

export default App;
