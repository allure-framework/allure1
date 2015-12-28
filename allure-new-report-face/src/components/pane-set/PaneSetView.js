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

    updatePane(name, changed, factory) {
        if(changed.hasOwnProperty(name)) {
            if(changed[name]) {
                this.addPane(name, factory());
            } else {
                this.removePane(name);
            }
        }
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
            this.fadeOutPane(this.panes[name], () => this.removeRegion(name));
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
        setTimeout(() => {
            pane.removeClass('pane_enter');
        });
    }

    fadeOutPane(pane, callback) {
        pane.addClass('pane_leave');
        pane.one('transitionend', () => {
            pane.remove();
            callback();
        });
    }
}

export default PaneSetView;
