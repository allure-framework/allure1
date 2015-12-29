import AppLayout from '../application/AppLayout';
import {Model} from 'backbone';
import XUnitCollection from '../../data/xunit/XUnitCollection';
import router from '../../router';
import TestcasePanes from '../../util/TestcasePanes';
import PaneSetView from '../../components/pane-set/PaneSetView';
import TestsuitesListView from '../../components/testsuites-list/TestsuitesListView';
import TestsuiteView from '../../components/testsuite-view/TestsuiteView';

export default class XUnitLayout extends AppLayout {

    initialize() {
        super.initialize();
        this.state = new Model();
        this.listenTo(this.state, 'change', this.onStateChange, this);
        this.suites = new XUnitCollection();
    }

    loadData() {
        return this.suites.fetch();
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
        if(!paneView.getRegion('testrun')) {
            paneView.addPane('testrun', new TestsuitesListView({
                collection: this.suites,
                state
            }));
        }
        paneView.updatePane('testsuite', changed, () => new TestsuiteView({
            testsuite: this.suites.findWhere({uid: changed.testsuite}),
            baseUrl: 'xUnit',
            state
        }));
        this.testcase.updatePanes('xUnit/' + this.state.get('testsuite'), changed);
        paneView.updatePanesPositions();
    }


    onRouteUpdate(testsuite, testcase, attachment) {
        const expanded = router.getUrlParams().expanded === 'true';
        this.state.set({testsuite, testcase, attachment, expanded});
    }
}
