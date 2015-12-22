import {LayoutView} from 'backbone.marionette';
import TestcaseModel from '../../data/testcase/TestcaseModel';
import template from './TestcaseView.hbs';

export default class TestcaseView extends LayoutView {
    template = template;

    initialize({testcase}) {
        this.model = new TestcaseModel({uid: testcase, fetched: false});
    }

    onRender() {
        if(!this.model.get('fetched')) {
            this.model.fetch().then(() => {
                this.model.set('fetched', true);
                this.render();
            });
        }
    }

    serializeData() {
        return Object.assign({
            route: {
                baseUrl: this.options.baseUrl
            }
        }, super.serializeData());
    }
}
