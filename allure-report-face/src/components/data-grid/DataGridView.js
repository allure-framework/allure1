import {LayoutView} from 'backbone.marionette';
import {on} from '../../decorators';
import {doSort, updateSort} from '../../util/sorting';
import settings from '../../util/settings';

class DataGridView extends LayoutView {
    settingsKey = null;

    getSettings() {
        return settings.get(this.settingsKey) || {field: 'title', order: 'asc'};
    }

    setSettings(sorting) {
        settings.save(this.settingsKey, sorting);
    }

    applySort(data) {
        return doSort(data, this.getSettings());
    }

    @on('click [data-sort]')
    onSortClick(e) {
        const el = this.$(e.currentTarget);
        const sorting = this.getSettings();
        this.setSettings(updateSort(el.data('sort'), sorting));
        this.render();
    }
}

export default DataGridView;
