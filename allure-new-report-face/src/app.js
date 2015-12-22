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
    function showView(factory) {
        return (...args) => App.getRegion('content').show(factory(...args));
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
