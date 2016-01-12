import './styles.css';
import {LayoutView} from 'backbone.marionette';
import $ from 'jquery';
import {className} from '../../decorators';
import allurePlugins from '../../pluginApi';

const widgetTpl = `<div class="widget"></div>`;

@className('widgets-grid')
class WidgetsGridView extends LayoutView {
    template() {
        return '';
    }

    onRender() {
        Object.keys(allurePlugins.widgets).forEach(widget => {
            this.addWidget(widget, allurePlugins.widgets[widget]);
        });
    }

    addWidget(name, Widget) {
        const el = $(widgetTpl);
        this.$el.append(el);
        this.addRegion(name, {el});
        this.getRegion(name).show(new Widget({model: this.model.getWidgetData(name)}));
    }
}

export default WidgetsGridView;
