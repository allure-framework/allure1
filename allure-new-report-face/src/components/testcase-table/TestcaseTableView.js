import {ItemView} from 'backbone.marionette';
import {on} from '../../decorators';
import router from '../../router';
import template from './TestcaseTableView.hbs';

export default class TestcaseTableView extends ItemView {
    template = template;

    @on('click .testcase-table__item')
    onTestcaseClick(e) {
        const testcase = this.$(e.currentTarget).data('uid');
        router.to(this.options.baseUrl + '/' + testcase);
    }

    serializeData() {
        return {
            currentCase: this.options.currentCase,
            testCases: this.options.testCases
        };
    }
}
