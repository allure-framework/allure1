import './styles.css';
import {ItemView} from 'backbone.marionette';
import {Model} from 'backbone';
import allurePlugins from '../../pluginApi';
import template from './SideNavView.hbs';

export default class SideNavView extends ItemView {
    template = template;

    initialize() {
        this.model = new Model({tabs: allurePlugins.tabs});
    }
}
