import {LayoutView} from 'backbone.marionette';
import {region} from '../../../decorators';
import template from './BehaviorView.hbs';
import TestcaseTableView from '../../../components/testcase-table/TestcaseTableView';

export default class BehaviorView extends LayoutView {
    template = template;

    @region('.behavior__testcases')
    testcases;

    initialize({state}) {
        this.state = state;
        this.listenTo(this.state, 'change:testcase', this.showTestcases, this);
    }

    onRender() {
        this.showTestcases();
    }

    showTestcases() {
        this.testcases.show(new TestcaseTableView({
            testCases: this.model.get('testCases'),
            currentCase: this.state.get('testcase'),
            baseUrl: 'behaviors/' + this.state.get('behavior')
        }));
    }

    serializeData() {
        return Object.assign({
            baseUrl: 'behaviors'
        }, super.serializeData());
    }
}
