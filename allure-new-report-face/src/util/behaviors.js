import {Behaviors} from 'backbone.marionette';
import LoadBehavior from '../behaviors/LoadBehavior';
import {Behavior as TabBehavior, ItemView as TabView} from 'marionette-tabs';


Behaviors.behaviorsLookup = {
    LoadBehavior,
    TabBehavior: (options, ...args) => new TabBehavior(Object.assign({
        tabCls: 'tabs__item',
        tabContainerCls: 'tabs__menu',
        selectedTabCls: 'tabs__item_selected',
        tabItemView: TabView.extend({
            template: (model) => '<a class="link link_theme_islands">' + model.title + '</i>'
        })
    }, options), ...args)
};
