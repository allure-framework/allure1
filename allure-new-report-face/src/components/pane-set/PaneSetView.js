import './styles.css';
import {LayoutView} from 'backbone.marionette';
import $ from 'jquery';
import {className} from '../../decorators';

const paneTpl = `<div class="pane"></div>`;

@className('pane-set')
class PaneSetView extends LayoutView {
    panes = [];
    template() { return ''; }

    pushPane(view) {
        const pane = $(paneTpl);
        this.$el.append(pane);
        this.addRegion(this.panes.length, {el: pane}).show(view);
        this.panes.push(pane);
    }

    popPane() {
        this.removeRegion(this.panes.length - 1);
        this.panes.pop().remove();
    }
}

export default PaneSetView;
