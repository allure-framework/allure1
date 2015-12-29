import {findWhere} from 'underscore';
import {Model} from 'backbone';
import AppLayout from '../application/AppLayout';
import router from '../../router';
import DefectsCollection from '../../data/defects/DefectsCollection';
import PaneSetView from '../../components/pane-set/PaneSetView';
import DefectsListView from '../../components/defects-list/DefectsListView';
import DefectView from '../../components/defect-view/DefectView';
import TestcasePanes from '../../util/TestcasePanes';

export default class DefectsLayoutView extends AppLayout {

    initialize() {
        super.initialize();
        this.state = new Model();
        this.listenTo(this.state, 'change', this.onStateChange, this);
        this.defects = new DefectsCollection();
    }

    loadData() {
        return this.defects.fetch();
    }

    getContentView() {
        return new PaneSetView();
    }

    onViewReady() {
        this.testcase = new TestcasePanes(this.state, this.content.currentView);
        this.onRouteUpdate(...this.options.routeParams);
    }

    buildDefectView(uid) {
        const defect = findWhere(this.defects.allDefects, {uid});
        if(!defect) {
            throw new Error(`Unable to find defect ${uid}`);
        }
        const model = new Model(defect);
        return new DefectView({model, state: this.state, baseUrl: 'defects'});
    }

    onStateChange() {
        const state = this.state;
        const changed = Object.assign({}, this.state.changed);
        const paneView = this.content.currentView;
        paneView.expanded = this.state.get('expanded');
        if(!paneView.getRegion('defects')) {
            paneView.addPane('defects', new DefectsListView({
                baseUrl: 'defects',
                collection: this.defects,
                state
            }));
        }
        paneView.updatePane('defect', changed, () => this.buildDefectView(changed.defect));
        this.testcase.updatePanes('defects/' + this.state.get('defect'), changed);
        paneView.updatePanesPositions();
    }

    onRouteUpdate(defect, testcase, attachment) {
        const expanded = router.getUrlParams().expanded === 'true';
        this.state.set({defect, testcase, attachment, expanded});
    }
}
