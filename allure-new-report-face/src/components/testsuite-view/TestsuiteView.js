import {LayoutView} from 'backbone.marionette';
import {on} from '../../decorators';
import router from '../../router';
import template from './TestsuiteView.hbs';

export default class TestsuiteView extends LayoutView {
    template = template;

    initialize({testsuite, state}) {
        this.state = state;
        this.listenTo(this.state, 'change:testcase', this.render);
        this.model = testsuite;
    }

    @on('click .testcase-table__item')
    onTestcaseClick(e) {
        const testsuite = this.state.get('testsuite');
        const testcase = this.$(e.currentTarget).data('uid');
        router.to(['xUnit', testsuite, testcase].join('/'));
    }

    serializeData() {
        return Object.assign({
            route: {
                baseUrl: this.options.baseUrl,
                currentCase: this.state.get('testcase')
            }
        }, super.serializeData());
    }
}
