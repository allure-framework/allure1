import {chain, findWhere} from 'underscore';
import {Model} from 'backbone';
import AppLayout from '../application/AppLayout';
import DefectsCollection from '../../data/defects/DefectsCollection';
import PaneSetView from '../../components/pane-set/PaneSetView';
import DefectsListView from '../../components/defects-list/DefectsListView';
import DefectView from '../../components/defect-view/DefectView';
import TestcaseView from '../../components/testcase-view/TestcaseView';

export default class DefectsLayoutView extends AppLayout {

    initialize({routeParams}) {
        const [defect, testcase, attachment] = routeParams;
        this.params = {defect, testcase, attachment};
        this.defects = new DefectsCollection();
    }

    loadData() {
        return this.defects.fetch().then(() => {
            this.allDefects = chain(this.defects.toJSON())
                .pluck('defects')
                .flatten()
                .value();
        });
    }

    getContentView() {
        return new PaneSetView();
    }

    onRender() {
        super.onRender().then(() => {
            const params = this.params;
            const paneView = this.content.currentView;
            paneView.pushPane(new DefectsListView({collection: this.defects, params}));
            if(params.defect) {
                const model = new Model(findWhere(this.allDefects, {uid: params.defect}));
                paneView.pushPane(new DefectView({model, params}));
            }
            if(params.testcase) {
                paneView.pushPane(new TestcaseView({
                    baseUrl: 'defects/' + params.defect,
                    testcase: params.testcase
                }));
            }
        });
    }
}
