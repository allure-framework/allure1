import './styles.css';
import {LayoutView} from 'backbone.marionette';
import template from './TestsuitesListView.hbs';

class TestsuitesListView extends LayoutView {
    template = template;

    initialize({state}) {
        this.state = state;
        this.listenTo(this.state, 'change:testsuite', this.render);
    }

    serializeData() {
        return {
            baseUrl: 'xUnit',
            currentSuite: this.state.get('testsuite'),
            time: this.collection.time,
            statistic: this.collection.statistic,
            suites: this.collection.toJSON()
        };
    }
}

export default TestsuitesListView;
