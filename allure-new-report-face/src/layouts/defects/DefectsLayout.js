import {chain, findWhere} from 'underscore';
import {Model} from 'backbone';
import AppLayout from '../application/AppLayout';
import DefectsCollection from '../../data/defects/DefectsCollection';
import PaneSetView from '../../components/pane-set/PaneSetView';
import DefectsListView from '../../components/defects-list/DefectsListView';
import DefectView from '../../components/defect-view/DefectView';
import TestcasePanes from '../../util/TestcasePanes';

export default class DefectsLayoutView extends AppLayout {

    initialize() {
        this.state = new Model();
        this.listenTo(this.state, 'change', this.onStateChange, this);
        this.defects = new DefectsCollection();
        this.on('load', this.onViewReady, this);
    }

    loadData() {
        return this.defects.fetch().then(() => {
            this.allDefects = chain(this.defects.toJSON())
                .map(type => {
                    type.defects.forEach(defect => {
                        defect.status = type.status;
                    });
                    return type.defects;
                })
                .flatten()
                .value();
        });
    }

    getContentView() {
        return new PaneSetView();
    }

    onViewReady() {
        this.testcase = new TestcasePanes(this.state, this.content.currentView);
        this.onRouteUpdate(...this.options.routeParams);
    }

    onStateChange() {
        const state = this.state;
        const changed = Object.assign({}, this.state.changed);
        const paneView = this.content.currentView;
        if(!paneView.getRegion('defects')) {
            paneView.addPane('defects', new DefectsListView({
                collection: this.defects,
                state
            }));
        }
        if(changed.hasOwnProperty('defect')) {
            if(!changed.defect) {
                paneView.removePane('defect');
            } else {
                const model = new Model(findWhere(this.allDefects, {uid: changed.defect}));
                paneView.addPane('defect', new DefectView({model, state}));
            }
        }
        this.testcase.updateState('defects/' + this.state.get('defect'), changed);
    }

    onRouteUpdate(defect, testcase, attachment) {
        this.state.set({defect, testcase, attachment});
    }
}
