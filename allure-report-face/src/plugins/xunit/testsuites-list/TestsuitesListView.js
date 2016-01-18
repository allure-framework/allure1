import './styles.css';
import {LayoutView} from 'backbone.marionette';
import {reduce} from 'underscore';
import StatusToggleView from '../../../components/status-toggle/StatusToggleView';
import {region} from '../../../decorators';
import settings from '../../../util/settings';
import template from './TestsuitesListView.hbs';

class TestsuitesListView extends LayoutView {
    template = template;

    @region('.testsuites-list__statuses')
    statuses;

    initialize({state}) {
        this.state = state;
        this.listenTo(this.state, 'change:testsuite', this.render);
        this.listenTo(settings, 'change:visibleStatuses', this.render);
    }

    onRender() {
        this.statuses.show(new StatusToggleView());
    }

    serializeData() {
        const statuses = settings.get('visibleStatuses');
        return {
            baseUrl: 'xUnit',
            currentSuite: this.state.get('testsuite'),
            time: this.collection.time,
            statistic: this.collection.statistic,
            suites: this.collection.toJSON().filter(suite => {
                return reduce(
                    suite.statistic,
                    (visible, value, status) => visible || (statuses[status.toUpperCase()] && value > 0),
                    false
                );
            })
        };
    }
}

export default TestsuitesListView;
