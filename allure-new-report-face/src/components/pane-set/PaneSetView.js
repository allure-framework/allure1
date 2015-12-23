import './styles.css';
import {each} from 'underscore';
import {LayoutView} from 'backbone.marionette';
import $ from 'jquery';
import {className} from '../../decorators';

const paneTpl = `<div class="pane"></div>`;

@className('pane-set')
class PaneSetView extends LayoutView {
    panes = {};

    template() {
        return '';
    }

    addPane(name, view) {
        if(!this.getRegion(name)) {
            const pane = $(paneTpl);
            this.fadeInPane(pane);
            this.panes[name] = pane;
            this.addRegion(name, {el: pane});
            this.updatePanesPositions();
        }
        this.getRegion(name).show(view);
    }

    removePane(name) {
        if(this.getRegion(name)) {
            this.removeRegion(name);
            this.fadeOutPane(this.panes[name]);
            delete this.panes[name];
            this.updatePanesPositions();
        }
    }

    updatePanesPositions() {
        const paneNames = Object.keys(this.panes);
        const last = paneNames.length - 1;
        paneNames.forEach((paneName, index) => {
            const pane = this.panes[paneName];
            var width;
            var left;
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

    fadeInPane(pane) {
        this.$el.append(pane);
        pane.addClass('pane_enter');
        pane.one('animationend', () => {
            pane.removeClass('pane_enter');
        });
    }

    fadeOutPane(pane) {
        pane.addClass('pane_leave');
        pane.one('animationend', () => {
            pane.remove();
        });
    }
}

export default PaneSetView;
