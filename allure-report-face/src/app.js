import 'font-awesome/css/font-awesome.css';
import './styles.css';

import {Application, Behaviors} from 'backbone.marionette';
import router from './router';
import TooltipBehavior from './util/TooltipBehavior';
import ErrorLayout from './layouts/error/ErrorLayout';

function rootPath(path) {
    return path.split('/')[0];
}

Behaviors.behaviorsLookup = {
    TooltipBehavior
};

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
