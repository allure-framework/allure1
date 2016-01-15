import './styles.css';
import {className} from '../../decorators';
import {ItemView} from 'backbone.marionette';
import {Model} from 'backbone';
import allurePlugins from '../../pluginApi';
import template from './SideNavView.hbs';
import router from '../../router';

@className('side-nav')
class SideNavView extends ItemView {
    template = template;

    initialize() {
        this.model = new Model({
            tabs: allurePlugins.tabs.map(({tabName, icon, title}) => ({
                tabName, icon, title,
                active: this.isTabActive(tabName)
            }))
        });
    }

    isTabActive(name) {
        var currentUrl = router.getCurrentUrl();
        return name ? currentUrl.indexOf(name) === 0 : currentUrl === name;
    }
}

export default SideNavView;
