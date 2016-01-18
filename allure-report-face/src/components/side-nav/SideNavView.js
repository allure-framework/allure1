import './styles.css';
import {className} from '../../decorators';
import {ItemView} from 'backbone.marionette';
import {Model} from 'backbone';
import ReportModel from '../../data/report/ReportModel';
import allurePlugins from '../../pluginApi';
import template from './SideNavView.hbs';
import router from '../../router';

@className('side-nav')
class SideNavView extends ItemView {
    static reportModel = new ReportModel();

    template = template;

    initialize() {
        this.model = this.constructor.reportModel;
        this.tabs = allurePlugins.tabs.map(({tabName, icon, title}) => ({
            tabName, icon, title,
            active: this.isTabActive(tabName)
        }));
        this.model.fetch().then(() => this.render())
    }

    serializeData() {
        return {
            tabs: this.tabs,
            report: this.model.toJSON()
        }
    }

    isTabActive(name) {
        var currentUrl = router.getCurrentUrl();
        return name ? currentUrl.indexOf(name) === 0 : currentUrl === name;
    }
}

export default SideNavView;
