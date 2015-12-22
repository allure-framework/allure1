import {ItemView} from 'backbone.marionette';
import {on} from '../../decorators';
import router from '../../router';
import template from './DefectView.hbs';

export default class DefectView extends ItemView {
    template = template;

    @on('click .testcase-table__item')
    onTestcaseClick(e) {
        const {defect} = this.options.params;
        const testcase = this.$(e.currentTarget).data('uid');
        router.to(['defects', defect, testcase].join('/'));
    }

    serializeData() {
        const {testcase} = this.options.params;
        const data = super.serializeData();
        data.testCases = data.testCases.map(t => {
            t.active = t.uid === testcase;
            return t;
        });
        return data;
    }
}
