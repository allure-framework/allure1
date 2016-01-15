import {LayoutView} from 'backbone.marionette';
import {region} from '../../../decorators';
import TestcaseTableView from '../../../components/testcase-table/TestcaseTableView';
import template from './DefectView.hbs';

export default class DefectView extends LayoutView {
    template = template;

    @region('.defect__table')
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
            baseUrl: 'defects/' + this.state.get('defect')
        }));
    }

    serializeData() {
        return Object.assign({
            baseUrl: this.options.baseUrl
        }, super.serializeData());
    }
}
