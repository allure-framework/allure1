import 'font-awesome/css/font-awesome.css';
import './styles.css';

import 'bem-components-dist/desktop/bem-components.dev.js+bh.js';
import {Application} from 'backbone.marionette';
import router from './router';
import ErrorLayout from './layouts/error/ErrorLayout';
import OverviewLayout from './layouts/overview/OverivewLayout';
import DefectsLayout from './layouts/defects/DefectsLayout';

const App = new Application({
    regions: {
        'error': '#alert',
        'content': '#content',
        'popup': '#popup'
    }
});

App.on('start', () => {
    function rootPath(path) {
        return path.split('/')[0];
    }
    function showView(factory) {
        var view;
        return (...args) => {
            if(view && rootPath(router.getCurrentUrl()) === rootPath(router.lastUrl)) {
                view.onRouteUpdate(...args);
            } else {
                view = factory(...args);
                App.getRegion('content').show(view);
            }
        };
    }
    router.on('route:home', showView(() => new OverviewLayout()));
    router.on('route:defects', showView((...routeParams) => new DefectsLayout({routeParams})));
    router.on('route:notFound', showView(() =>
        new ErrorLayout({
            code: 404,
            message: 'Not Found'
        })
    ));
});


export default App;
