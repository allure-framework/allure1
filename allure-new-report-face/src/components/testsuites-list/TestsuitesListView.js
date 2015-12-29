//import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {on} from '../../decorators';
import router from '../../router';
import template from './TestsuitesListView.hbs';

class TestsuitesListView extends LayoutView {
    template = template;

    initialize({state}) {
        this.state = state;
        this.listenTo(this.state, 'change:testsuite', this.render);
    }

    @on('click .testsuite-list__row')
    onSuiteRowClick(e) {
        const suiteUid = this.$(e.currentTarget).data('uid');
        router.to('xUnit/'+suiteUid);
    }

    serializeData() {
        return {
            currentSuite: this.state.get('testsuite'),
            time: this.collection.time,
            statistic: this.collection.statistic,
            suites: this.collection.toJSON()
        };
    }
}

export default TestsuitesListView;
