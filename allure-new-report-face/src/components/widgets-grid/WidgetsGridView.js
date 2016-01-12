import './styles.css';
import {LayoutView} from 'backbone.marionette';
import $ from 'jquery';
import {className} from '../../decorators';
import allurePlugins from '../../pluginApi';

const widgetTpl = `<div class="widget"></div>`;
const colTpl = `<div class="widgets-grid__col"></div>`;

@className('widgets-grid')
class WidgetsGridView extends LayoutView {
    template() {
        return '';
    }

    onRender() {
        Object.keys(allurePlugins.widgets).map(widget => {
            return [widget, allurePlugins.widgets[widget]];
        }).reduce((widgets, widgetInfo, index) => {
            widgets[index % 2].push(widgetInfo);
            return widgets;
        }, [[], []]).forEach(widgetCol => {
            const col = $(colTpl);
            this.$el.append(col);
            widgetCol.forEach(([name, Widget]) => {
                this.addWidget(col, name, Widget);
            });
        });
    }

    addWidget(col, name, Widget) {
        const el = $(widgetTpl);
        col.append(el);
        this.addRegion(name, {el});
        this.getRegion(name).show(new Widget({model: this.model.getWidgetData(name)}));
    }
}

export default WidgetsGridView;
