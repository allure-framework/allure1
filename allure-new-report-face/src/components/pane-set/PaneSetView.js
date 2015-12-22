import './styles.css';
import {LayoutView} from 'backbone.marionette';
import $ from 'jquery';
import {className} from '../../decorators';

const paneTpl = `<div class="pane"></div>`;

@className('pane-set')
class PaneSetView extends LayoutView {
    panes = [];

    template() {
        return '';
    }

    pushPane(view) {
        const pane = $(paneTpl);
        this.$el.append(pane);
        this.addRegion(this.panes.length, {el: pane}).show(view);
        this.panes.push(pane);
        this.updatePanesPositions();
    }

    popPane() {
        this.removeRegion(this.panes.length - 1);
        this.panes.pop().remove();
        this.updatePanesPositions();
    }

    updatePanesPositions() {
        const last = this.panes.length - 1;
        this.panes.forEach((pane, index) => {
            var width, left;
            if(index == last) {
                width = index === 0 ? 100 : 50;
                left = 100 - width;
            } else {
                const leftOffset = 5 * index;
                left = leftOffset;
                width = 50 - leftOffset;
            }
            pane.css({left: left + '%', width: width + '%'});
        });
    }
}

export default PaneSetView;
