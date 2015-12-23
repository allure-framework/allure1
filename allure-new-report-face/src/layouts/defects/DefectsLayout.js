import {chain, findWhere} from 'underscore';
import {Model} from 'backbone';
import AppLayout from '../application/AppLayout';
import DefectsCollection from '../../data/defects/DefectsCollection';
import TestcaseModel from '../../data/testcase/TestcaseModel';
import PaneSetView from '../../components/pane-set/PaneSetView';
import DefectsListView from '../../components/defects-list/DefectsListView';
import DefectView from '../../components/defect-view/DefectView';
import TestcaseView from '../../components/testcase-view/TestcaseView';
import AttachmentView from '../../components/attachment/AttachmentView';

export default class DefectsLayoutView extends AppLayout {

    initialize() {
        this.state = new Model();
        this.listenTo(this.state, 'change', this.onStateChange, this);
        this.defects = new DefectsCollection();
        this.testcase = new TestcaseModel();
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
        if(changed.hasOwnProperty('testcase')) {
            paneView.removePane('attachment');
            if(!changed.testcase) {
                paneView.removePane('testcase');
            } else {
                this.testcase.set({uid: changed.testcase});
                this.testcase.fetch().then(() => this.renderTestcasePanes(changed));
            }
        } else if(changed.hasOwnProperty('attachment')) {
            if(!changed.attachment) {
                paneView.removePane('attachment');
            } else {
                paneView.addPane('attachment', new AttachmentView({
                    baseUrl: 'defects/' + this.state.get('defect') + '/' + this.state.get('testcase'),
                    attachment: this.testcase.getAttachment(changed.attachment),
                    state
                }));
            }
        }
    }

    renderTestcasePanes(changed) {
        const paneView = this.content.currentView;
        const state = this.state;
        paneView.addPane('testcase', new TestcaseView({
            baseUrl: 'defects/' + this.state.get('defect'),
            model: this.testcase,
            state
        }));
        if(changed.hasOwnProperty('attachment')) {
            if(!changed.attachment) {
                paneView.removePane('attachment');
            } else {
                paneView.addPane('attachment', new AttachmentView({
                    baseUrl: 'defects/' + this.state.get('defect') + '/' + this.state.get('testcase'),
                    attachment: this.testcase.getAttachment(changed.attachment),
                    state
                }));
            }
        }

    }


    onRouteUpdate(defect, testcase, attachment) {
        this.state.set({defect, testcase, attachment});
    }
}
