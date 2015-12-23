import './styles.css';
import {LayoutView} from 'backbone.marionette';
import bemRender from '../../util/bemRender';
import {on, region} from '../../decorators';
import TestcaseModel from '../../data/testcase/TestcaseModel';
import DescriptionView from '../description/DescriptionView';
import template from './TestcaseView.hbs';

class TestcaseView extends LayoutView {
    template = template;

    @region('.testcase__description')
    description;

    initialize({testcase}) {
        this.model = new TestcaseModel({uid: testcase, fetched: false});
    }

    onRender() {
        if(!this.model.get('fetched')) {
            this.model.fetch().then(() => {
                this.model.set('fetched', true);
                this.render();
            });
        } else {
            bemRender(this.$('.testcase__trace-toggle'), {
                block: 'button',
                mods: {view: 'pseudo', inverse: true},
                text: 'Show trace'
            });
            if(this.model.has('description')) {
                this.description.show(new DescriptionView({description: this.model.get('description')}));
            }
        }
    }

    serializeData() {
        return Object.assign({
            route: {
                baseUrl: this.options.baseUrl
            }
        }, super.serializeData());
    }

    @on('click .testcase__trace-toggle')
    onStacktraceClick() {
        this.$('.testcase__trace').toggleClass('testcase__trace_visible');
    }
}

export default TestcaseView;
