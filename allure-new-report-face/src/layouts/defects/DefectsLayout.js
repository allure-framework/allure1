import AppLayout from '../application/AppLayout';
import DefectsCollection from '../../data/defects/DefectsCollection';
import DefectsListView from '../../components/defects-list/DefectsListView';

export default class DefectsLayoutView extends AppLayout {

    initialize() {
        this.defects = new DefectsCollection();
    }

    loadData() {
        return this.defects.fetch();
    }

    getContentView() {
        return new DefectsListView({collection: this.defects});
    }
}
