import {chain, findWhere} from 'underscore';
import {Model} from 'backbone';
import AppLayout from '../application/AppLayout';
import DefectsCollection from '../../data/defects/DefectsCollection';
import PaneSetView from '../../components/pane-set/PaneSetView';
import DefectsListView from '../../components/defects-list/DefectsListView';
import DefectView from '../../components/defect-view/DefectView';
import TestcaseView from '../../components/testcase-view/TestcaseView';

export default class DefectsLayoutView extends AppLayout {

    initialize() {
        this.state = new Model();
        this.listenTo(this.state, 'change', this.onStateChange, this);
        this.defects = new DefectsCollection();
        this.on('load', () => this.onRouteUpdate(...this.options.routeParams));
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

    onStateChange() {
        const state = this.state;
        const changed = this.state.changed;
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
        if(changed.hasOwnProperty('testcase')) {
            if(!changed.testcase) {
                paneView.removePane('testcase');
            } else {
                paneView.addPane('testcase', new TestcaseView({
                    baseUrl: 'defects/' + this.state.get('defect'),
                    testcase: changed.testcase,
                    state
                }));
            }
        }
    }


    onRouteUpdate(defect, testcase, attachment) {
        this.state.set({defect, testcase, attachment});
    }
}
