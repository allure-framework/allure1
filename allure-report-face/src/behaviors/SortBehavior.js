import {Behavior} from 'backbone.marionette';
import {updateSort} from '../util/sorting';
import {on} from '../decorators';

export default class SortBehavior extends Behavior {

    @on('click [data-sort]')
    onSortClick(e) {
        const el = this.$(e.currentTarget);
        const sorting = this.view.getSettings();
        this.view.setSettings(updateSort(el.data('sort'), sorting));
        this.view.render();
    }

}
