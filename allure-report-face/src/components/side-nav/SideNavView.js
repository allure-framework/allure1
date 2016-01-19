import './styles.css';
import {className, on} from '../../decorators';
import {ItemView} from 'backbone.marionette';
import ReportModel from '../../data/report/ReportModel';
import allurePlugins from '../../pluginApi';
import settings from '../../util/settings';
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
        this.model.fetch().then(() => this.render());
    }

    onRender() {
        this.$el.toggleClass('side-nav_collapsed', settings.get('sidebarCollapsed'));
    }

    serializeData() {
        return {
            tabs: this.tabs,
            report: this.model.toJSON()
        };
    }

    isTabActive(name) {
        var currentUrl = router.getCurrentUrl();
        return name ? currentUrl.indexOf(name) === 0 : currentUrl === name;
    }

    @on('click .side-nav__collapse')
    onCollapseClick() {
        this.$el.toggleClass('side-nav_collapsed');
        settings.save('sidebarCollapsed', this.$el.hasClass('side-nav_collapsed'));
    }
}

export default SideNavView;
