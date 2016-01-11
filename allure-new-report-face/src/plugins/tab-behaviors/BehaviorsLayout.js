import {Model} from 'backbone';
import BehaviorsModel from './data/BehaviorsModel';
import AppLayout from '../../layouts/application/AppLayout';
import router from '../../router';
import PaneSetView from '../../components/pane-set/PaneSetView';
import BehaviorsTreeView from './behaviors-tree/BehaviorsTreeView';
import BehaviorView from './behavior-view/BehaviorView';
import TestcasePanes from '../../util/TestcasePanes';

export default class BehaviorsLayout extends AppLayout {

    initialize() {
        super.initialize();
        this.state = new Model();
        this.listenTo(this.state, 'change', this.onStateChange, this);
        this.behaviors = new BehaviorsModel();
    }

    loadData() {
        return this.behaviors.fetch();
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
        paneView.expanded = this.state.get('expanded');
        if(!paneView.getRegion('behaviors')) {
            paneView.addPane('behaviors', new BehaviorsTreeView({
                model: this.behaviors,
                state
            }));
        }
        paneView.updatePane('behavior', changed, () => new BehaviorView({
            model: this.behaviors.getBehavior(this.state.get('behavior')),
            state
        }));
        this.testcase.updatePanes('behaviors/' + this.state.get('behavior'), changed);
        paneView.updatePanesPositions();
    }

    onRouteUpdate(behavior, testcase, attachment) {
        const expanded = router.getUrlParams().expanded === 'true';
        this.state.set({behavior, testcase, attachment, expanded});
    }
}
