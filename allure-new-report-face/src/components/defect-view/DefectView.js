import './styles.css';
import {ItemView} from 'backbone.marionette';
import {on} from '../../decorators';
import router from '../../router';
import template from './DefectView.hbs';

export default class DefectView extends ItemView {
    template = template;

    initialize({state}) {
        this.state = state;
        this.listenTo(this.state, 'change:testcase', () => this.render());
    }

    @on('click .testcase-table__item')
    onTestcaseClick(e) {
        const defect = this.state.get('defect');
        const testcase = this.$(e.currentTarget).data('uid');
        router.to(['defects', defect, testcase].join('/'));
    }

    serializeData() {
        const testcase = this.state.get('testcase');
        const data = super.serializeData();
        data.baseUrl = this.options.baseUrl;
        data.testCases = data.testCases.map(t => {
            t.active = t.uid === testcase;
            return t;
        });
        return data;
    }
}
